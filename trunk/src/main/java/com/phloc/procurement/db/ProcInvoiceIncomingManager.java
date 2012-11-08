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
import com.phloc.db.jpa.IEntityManagerProvider;
import com.phloc.procurement.domain.EProcState;
import com.phloc.procurement.invoice.ProcInvoiceIncoming;

public class ProcInvoiceIncomingManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcInvoiceIncomingManager.class);

  public ProcInvoiceIncomingManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ESuccess saveInvoice (@Nonnull final ProcInvoiceIncoming aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aInvoice);
        s_aLogger.info ("Incoming invoice saved: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess deleteInvoice (@Nonnull final ProcInvoiceIncoming aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aInvoice.getInvoice ().setDeleted (true);
        s_aLogger.info ("Incoming invoice deleted: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess undeleteInvoice (@Nonnull final ProcInvoiceIncoming aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aInvoice.getInvoice ().setDeleted (false);
        s_aLogger.info ("Incoming invoice undeleted: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess acceptInvoice (@Nonnull final ProcInvoiceIncoming aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        // Change state in DB
        aInvoice.setState (EProcState.ACCEPTED);
        s_aLogger.info ("Incoming invoice accepted: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess rejectInvoice (@Nonnull final ProcInvoiceIncoming aInvoice)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        // Change state in DB
        aInvoice.setState (EProcState.REJECTED);
        s_aLogger.info ("Incoming invoice reject: " + aInvoice);
      }
    });
  }

  @Nonnull
  public final ESuccess changeInvoiceState (final ProcInvoiceIncoming aInvoice, final EProcState eNewState)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aInvoice.setState (eNewState);
        s_aLogger.info ("Incoming invoice state changed to " + eNewState + ": " + aInvoice);
      }
    });
  }

  @Nonnull
  public final List <ProcInvoiceIncoming> getAllInvoices ()
  {
    return getAllInvoices (null);
  }

  @Nonnull
  public final List <ProcInvoiceIncoming> getAllInvoices (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcInvoiceIncoming>> ()
    {
      public final List <ProcInvoiceIncoming> call ()
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
  public final long getCountAllInvoices (@Nullable final EProcState eState)
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
  public final List <ProcInvoiceIncoming> getAllDeletedInvoices ()
  {
    return getAllDeletedInvoices (null);
  }

  @Nonnull
  public final List <ProcInvoiceIncoming> getAllDeletedInvoices (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcInvoiceIncoming>> ()
    {
      public final List <ProcInvoiceIncoming> call ()
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
  public final long getCountAllDeletedInvoices (final EProcState eState)
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (_getCountAll (true, eState));
      }
    }).get ().longValue ();
  }

  public final long getCountOfNewInvoices ()
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (getEntityManager ().createQuery ("SELECT COUNT(p) FROM ProcInvoiceIncoming p"
                                                                         + " WHERE p.invoice.deleted = false AND p.state = :state")
                                                           .setParameter ("state", EProcState.RECEIVED));
      }
    }).get ().longValue ();
  }

  private TypedQuery <ProcInvoiceIncoming> _getQueryAll (final boolean bDeleted, final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT p FROM ProcInvoiceIncoming p WHERE p.invoice.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final TypedQuery <ProcInvoiceIncoming> aQuery = getEntityManager ().createQuery (aQueryString.toString (),
                                                                                     ProcInvoiceIncoming.class);
    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  private Query _getCountAll (final boolean bDeleted, final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT COUNT(p) FROM ProcInvoiceIncoming p WHERE p.invoice.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final Query aQuery = getEntityManager ().createQuery (aQueryString.toString ());
    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  @Nullable
  public final ProcInvoiceIncoming getActiveInvoiceOfID (final int nInvoiceID)
  {
    // Avoid query...
    if (nInvoiceID < 0)
      return null;

    return doSelect (new Callable <ProcInvoiceIncoming> ()
    {
      public final ProcInvoiceIncoming call ()
      {
        final ProcInvoiceIncoming aInvoice = getEntityManager ().find (ProcInvoiceIncoming.class,
                                                                       Integer.valueOf (nInvoiceID));
        return aInvoice == null || aInvoice.getInvoice () == null || aInvoice.getInvoice ().isDeleted () ? null
                                                                                                        : aInvoice;
      }
    }).get ();
  }

  @Nullable
  public final ProcInvoiceIncoming getAnyInvoiceOfID (final int nInvoiceID)
  {
    // Avoid query...
    if (nInvoiceID < 0)
      return null;

    return doSelect (new Callable <ProcInvoiceIncoming> ()
    {
      public final ProcInvoiceIncoming call ()
      {
        return getEntityManager ().find (ProcInvoiceIncoming.class, Integer.valueOf (nInvoiceID));
      }
    }).get ();
  }
}
