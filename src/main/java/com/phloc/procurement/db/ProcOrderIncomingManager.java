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
import com.phloc.procurement.order.ProcOrderIncoming;

public class ProcOrderIncomingManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcOrderIncomingManager.class);

  public ProcOrderIncomingManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ESuccess saveOrder (@Nonnull final ProcOrderIncoming aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aOrder);
        s_aLogger.info ("Incoming order saved: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess deleteOrder (@Nonnull final ProcOrderIncoming aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aOrder.getOrder ().setDeleted (true);
        s_aLogger.info ("Incoming order deleted: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess acceptOrder (@Nonnull final ProcOrderIncoming aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        // Change state in DB
        aOrder.setState (EProcState.ACCEPTED);
        s_aLogger.info ("Incoming order accepted: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess rejectOrder (@Nonnull final ProcOrderIncoming aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        // Change state in DB
        aOrder.setState (EProcState.REJECTED);
        s_aLogger.info ("Incoming order reject: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess undeleteOrder (@Nonnull final ProcOrderIncoming aOrder)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aOrder.getOrder ().setDeleted (false);
        s_aLogger.info ("Incoming order undeleted: " + aOrder);
      }
    });
  }

  @Nonnull
  public final ESuccess changeOrderState (final ProcOrderIncoming aOrder, final EProcState eNewState)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aOrder.setState (eNewState);
        s_aLogger.info ("Incoming order state changed to " + eNewState + ": " + aOrder);
      }
    });
  }

  @Nonnull
  public final List <ProcOrderIncoming> getAllOrders ()
  {
    return getAllOrders (null);
  }

  @Nonnull
  public final List <ProcOrderIncoming> getAllOrders (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcOrderIncoming>> ()
    {
      public final List <ProcOrderIncoming> call ()
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
  public final List <ProcOrderIncoming> getAllDeletedOrders ()
  {
    return getAllDeletedOrders (null);
  }

  @Nonnull
  public final List <ProcOrderIncoming> getAllDeletedOrders (@Nullable final EProcState eState)
  {
    return doSelect (new Callable <List <ProcOrderIncoming>> ()
    {
      public final List <ProcOrderIncoming> call ()
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

  @Nonnegative
  public final long getCountOfNewOrders ()
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (getEntityManager ().createQuery ("SELECT COUNT(p) FROM ProcOrderIncoming p"
                                                                         + " WHERE p.order.deleted = false AND p.state = :state")
                                                           .setParameter ("state", EProcState.RECEIVED));
      }
    }).get ().longValue ();
  }

  private TypedQuery <ProcOrderIncoming> _getQueryAll (final boolean bDeleted, final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT p FROM ProcOrderIncoming p WHERE p.order.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final TypedQuery <ProcOrderIncoming> aQuery = getEntityManager ().createQuery (aQueryString.toString (),
                                                                                   ProcOrderIncoming.class);
    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  private Query _getCountAll (final boolean bDeleted, final EProcState eState)
  {
    final StringBuilder aQueryString = new StringBuilder ("SELECT COUNT(p) FROM ProcOrderIncoming p WHERE p.order.deleted = :deleted");
    if (eState != null)
      aQueryString.append (" AND p.state = :state");

    final Query aQuery = getEntityManager ().createQuery (aQueryString.toString ());
    aQuery.setParameter ("deleted", Boolean.valueOf (bDeleted));
    if (eState != null)
      aQuery.setParameter ("state", eState);
    return aQuery;
  }

  @Nullable
  public final ProcOrderIncoming getActiveOrderOfID (final int nOrderID)
  {
    // Avoid query...
    if (nOrderID < 0)
      return null;

    return doSelect (new Callable <ProcOrderIncoming> ()
    {
      public final ProcOrderIncoming call ()
      {
        final ProcOrderIncoming aOrder = getEntityManager ().find (ProcOrderIncoming.class, Integer.valueOf (nOrderID));
        return aOrder == null || aOrder.getOrder () == null || aOrder.getOrder ().isDeleted () ? null : aOrder;
      }
    }).get ();
  }

  @Nullable
  public final ProcOrderIncoming getAnyOrderOfID (final int nOrderID)
  {
    // Avoid query...
    if (nOrderID < 0)
      return null;

    return doSelect (new Callable <ProcOrderIncoming> ()
    {
      public final ProcOrderIncoming call ()
      {
        return getEntityManager ().find (ProcOrderIncoming.class, Integer.valueOf (nOrderID));
      }
    }).get ();
  }
}
