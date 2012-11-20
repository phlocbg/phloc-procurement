/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.procurement.db;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.db.jpa.IEntityManagerProvider;
import com.phloc.procurement.domain.EProcState;
import com.phloc.procurement.invoice.ProcInvoiceOutgoing;

public class ProcInvoiceOutgoingManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcInvoiceOutgoingManager.class);

  public ProcInvoiceOutgoingManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ESuccess saveInvoice (@Nonnull final ProcInvoiceOutgoing aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aInvoice);
        s_aLogger.info ("Outgoing invoice saved: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess deleteInvoice (@Nonnull final ProcInvoiceOutgoing aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aInvoice.getInvoice ().setDeleted (true);
        s_aLogger.info ("Outgoing invoice deleted: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess undeleteInvoice (@Nonnull final ProcInvoiceOutgoing aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aInvoice.getInvoice ().setDeleted (false);
        s_aLogger.info ("Outgoing invoice undeleted: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess changeInvoiceState (final ProcInvoiceOutgoing aInvoice, final EProcState eNewState)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aInvoice.setState (eNewState);
        s_aLogger.info ("Outgoing invoice state changed to " + eNewState + ": " + aInvoice);
      }
    });
  }

  @Nullable
  public final ProcInvoiceOutgoing getActiveInvoiceOfInvoiceNumber (@Nullable final String sInvoiceNumber)
  {
    if (StringHelper.hasNoText (sInvoiceNumber))
      return null;

    return doSelect (new Callable <ProcInvoiceOutgoing> ()
    {
      @Nullable
      public ProcInvoiceOutgoing call () throws Exception
      {
        final List <ProcInvoiceOutgoing> aTmp = getEntityManager ().createQuery ("SELECT p FROM ProcInvoiceOutgoing p"
                                                                                     + " WHERE p.invoice.deleted = false AND p.invoice.invoiceNumber = :invoiceNumber",
                                                                                 ProcInvoiceOutgoing.class)
                                                                   .setParameter ("invoiceNumber", sInvoiceNumber)
                                                                   .getResultList ();
        if (aTmp.isEmpty ())
          return null;
        if (aTmp.size () > 1)
          s_aLogger.warn ("Too many results (" + aTmp.size () + ") for " + sInvoiceNumber);
        return aTmp.get (0);
      }
    }).get ();
  }

  @Nonnull
  public final List <ProcInvoiceOutgoing> getAllInvoices ()
  {
    return getAllInvoices (null);
  }

  @Nonnull
  public final List <ProcInvoiceOutgoing> getAllInvoices (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcInvoiceOutgoing>> ()
    {
      public final List <ProcInvoiceOutgoing> call ()
      {
        return _getQueryAll (false, eState).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllInvoices ()
  {
    return getCountAllInvoices (null);
  }

  @Nonnegative
  public final long getCountAllInvoices (final EProcState eState)
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (_getCountAll (false, eState));
      }
    }).get ().longValue ();
  }

  @Nonnull
  public final List <ProcInvoiceOutgoing> getAllDeletedInvoices ()
  {
    return getAllDeletedInvoices (null);
  }

  @Nonnull
  public final List <ProcInvoiceOutgoing> getAllDeletedInvoices (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcInvoiceOutgoing>> ()
    {
      public final List <ProcInvoiceOutgoing> call ()
      {
        return _getQueryAll (true, eState).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllDeletedInvoices ()
  {
    return getCountAllDeletedInvoices (null);
  }

  @Nonnegative
  public final long getCountAllDeletedInvoices (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (_getCountAll (true, eState));
      }
    }).get ().longValue ();
  }

  @Nonnull
  private TypedQuery <ProcInvoiceOutgoing> _getQueryAll (final boolean bDeleted, final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT p FROM ProcInvoiceOutgoing p WHERE p.invoice.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final TypedQuery <ProcInvoiceOutgoing> aQuery = getEntityManager ().createQuery (aQueryString.toString (),
                                                                                     ProcInvoiceOutgoing.class);
    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  @Nonnull
  private Query _getCountAll (final boolean bDeleted, final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT COUNT(p) FROM ProcInvoiceOutgoing p WHERE p.invoice.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final Query aQuery = getEntityManager ().createQuery (aQueryString.toString ());
    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  @Nullable
  public final ProcInvoiceOutgoing getActiveInvoiceOfID (final int nInvoiceID)
  {
    // Avoid query...
    if (nInvoiceID < 0)
      return null;

    return doSelect (new Callable <ProcInvoiceOutgoing> ()
    {
      public final ProcInvoiceOutgoing call ()
      {
        final ProcInvoiceOutgoing aInvoice = getEntityManager ().find (ProcInvoiceOutgoing.class,
                                                                       Integer.valueOf (nInvoiceID));
        return aInvoice == null || aInvoice.getInvoice () == null || aInvoice.getInvoice ().isDeleted () ? null
                                                                                                        : aInvoice;
      }
    }).get ();
  }

  @Nullable
  public final ProcInvoiceOutgoing getAnyInvoiceOfID (final int nInvoiceID)
  {
    // Avoid query...
    if (nInvoiceID < 0)
      return null;

    return doSelect (new Callable <ProcInvoiceOutgoing> ()
    {
      public final ProcInvoiceOutgoing call ()
      {
        return getEntityManager ().find (ProcInvoiceOutgoing.class, Integer.valueOf (nInvoiceID));
      }
    }).get ();
  }
}
