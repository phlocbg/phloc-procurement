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
import com.phloc.procurement.order.ProcOrderOutgoing;

public class ProcOrderOutgoingManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcOrderOutgoingManager.class);

  public ProcOrderOutgoingManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ESuccess saveOrder (@Nonnull final ProcOrderOutgoing aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aOrder);
        s_aLogger.info ("Outgoing order saved: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess deleteOrder (@Nonnull final ProcOrderOutgoing aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aOrder.getOrder ().setDeleted (true);
        s_aLogger.info ("Outgoing order deleted: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess undeleteOrder (@Nonnull final ProcOrderOutgoing aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aOrder.getOrder ().setDeleted (false);
        s_aLogger.info ("Outgoing order undeleted: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess changeOrderState (final ProcOrderOutgoing aOrder, final EProcState eNewState)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aOrder.setState (eNewState);
        s_aLogger.info ("Outgoing order state changed to " + eNewState + ": " + aOrder);
      }
    });
  }

  @Nullable
  public final ProcOrderOutgoing getActiveOrderOfOrderNumber (@Nullable final String sOrderNumber)
  {
    if (StringHelper.hasNoText (sOrderNumber))
      return null;

    return doSelect (new Callable <ProcOrderOutgoing> ()
    {
      @Nullable
      public ProcOrderOutgoing call () throws Exception
      {
        final List <ProcOrderOutgoing> aTmp = getEntityManager ().createQuery ("SELECT p FROM ProcOrderOutgoing p"
                                                                                   + " WHERE p.order.deleted = false AND p.order.orderNumber = :orderNumber",
                                                                               ProcOrderOutgoing.class)
                                                                 .setParameter ("orderNumber", sOrderNumber)
                                                                 .getResultList ();
        if (aTmp.isEmpty ())
          return null;
        if (aTmp.size () > 1)
          s_aLogger.warn ("Too many results (" + aTmp.size () + ") for " + sOrderNumber);
        return aTmp.get (0);
      }
    }).get ();
  }

  @Nonnull
  public final List <ProcOrderOutgoing> getAllOrders ()
  {
    return getAllOrders (null);
  }

  @Nonnull
  public final List <ProcOrderOutgoing> getAllOrders (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcOrderOutgoing>> ()
    {
      public final List <ProcOrderOutgoing> call ()
      {
        return _getQueryAll (false, eState).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllOrders ()
  {
    return getCountAllOrders (null);
  }

  @Nonnegative
  public final long getCountAllOrders (@Nullable final EProcState eState)
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
  public final List <ProcOrderOutgoing> getAllDeletedOrders ()
  {
    return getAllDeletedOrders (null);
  }

  @Nonnull
  public final List <ProcOrderOutgoing> getAllDeletedOrders (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcOrderOutgoing>> ()
    {
      public final List <ProcOrderOutgoing> call ()
      {
        return _getQueryAll (true, eState).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllDeletedOrders ()
  {
    return getCountAllDeletedOrders (null);
  }

  @Nonnegative
  public final long getCountAllDeletedOrders (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (_getCountAll (true, eState));
      }
    }).get ().longValue ();
  }

  private TypedQuery <ProcOrderOutgoing> _getQueryAll (final boolean bDeleted, @Nullable final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT p FROM ProcOrderOutgoing p WHERE p.order.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final TypedQuery <ProcOrderOutgoing> aQuery = getEntityManager ().createQuery (aQueryString.toString (),
                                                                                   ProcOrderOutgoing.class);

    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  private Query _getCountAll (final boolean bDeleted, @Nullable final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT COUNT(p) FROM ProcOrderOutgoing p WHERE p.order.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final Query aQuery = getEntityManager ().createQuery (aQueryString.toString ());

    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  @Nullable
  public final ProcOrderOutgoing getActiveOrderOfID (final int nOrderID)
  {
    // Avoid query...
    if (nOrderID < 0)
      return null;

    return doSelect (new Callable <ProcOrderOutgoing> ()
    {
      public final ProcOrderOutgoing call ()
      {
        final ProcOrderOutgoing aOrder = getEntityManager ().find (ProcOrderOutgoing.class, Integer.valueOf (nOrderID));
        return aOrder == null || aOrder.getOrder () == null || aOrder.getOrder ().isDeleted () ? null : aOrder;
      }
    }).get ();
  }

  @Nullable
  public final ProcOrderOutgoing getAnyOrderOfID (final int nOrderID)
  {
    // Avoid query...
    if (nOrderID < 0)
      return null;

    return doSelect (new Callable <ProcOrderOutgoing> ()
    {
      public final ProcOrderOutgoing call ()
      {
        return getEntityManager ().find (ProcOrderOutgoing.class, Integer.valueOf (nOrderID));
      }
    }).get ();
  }
}
