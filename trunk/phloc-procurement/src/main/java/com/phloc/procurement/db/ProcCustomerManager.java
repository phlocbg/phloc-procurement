/**
 * Copyright (C) 2006-2014 phloc systems
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.db.jpa.IEntityManagerProvider;
import com.phloc.procurement.idscheme.IEntityIDScheme;
import com.phloc.procurement.party.IProcCustomer;
import com.phloc.procurement.party.ProcCustomer;

public class ProcCustomerManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcCustomerManager.class);

  public ProcCustomerManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ISuccessIndicator saveCustomer (@Nonnull final IProcCustomer aCustomer)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aCustomer);
        s_aLogger.info ("Customer saved: " + aCustomer);
      }
    });
  }

  @Nonnull
  public final ISuccessIndicator deleteCustomer (@Nonnull final ProcCustomer aCustomer)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aCustomer.setDeleted (true);
        s_aLogger.info ("Customer deleted: " + aCustomer);
      }
    });
  }

  @Nonnull
  public final ISuccessIndicator undeleteCustomer (@Nonnull final ProcCustomer aCustomer)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aCustomer.setDeleted (false);
        s_aLogger.info ("Customer undeleted: " + aCustomer);
      }
    });
  }

  @Nullable
  public final ProcCustomer getCustomerFromEntityID (final IEntityIDScheme aEntityIDScheme, final String sEntityID)
  {
    return doSelect (new Callable <ProcCustomer> ()
    {
      @Nullable
      public final ProcCustomer call ()
      {
        final List <ProcCustomer> aTmp = getEntityManager ().createQuery ("SELECT p FROM ProcCustomer p"
                                                                              + " WHERE p.deleted = false AND p.entityIDScheme = :scheme AND p.entityID = :value",
                                                                          ProcCustomer.class)
                                                            .setParameter ("scheme", aEntityIDScheme)
                                                            .setParameter ("value", sEntityID)
                                                            .getResultList ();
        if (aTmp.isEmpty ())
          return null;
        if (aTmp.size () > 1)
          s_aLogger.warn ("Too many results (" + aTmp.size () + ") for " + aEntityIDScheme + "/" + sEntityID);
        return aTmp.get (0);
      }
    }).get ();
  }

  @Nonnull
  public final List <ProcCustomer> getAllCustomers ()
  {
    return doSelect (new Callable <List <ProcCustomer>> ()
    {
      public final List <ProcCustomer> call ()
      {
        return getEntityManager ().createQuery ("SELECT p FROM ProcCustomer p" + " WHERE p.deleted = false",
                                                ProcCustomer.class).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllCustomers ()
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (getEntityManager ().createQuery ("SELECT COUNT(p) FROM ProcCustomer p"
                                                                         + " WHERE p.deleted = false"));
      }
    }).get ().longValue ();
  }

  @Nonnull
  public final List <ProcCustomer> getAllDeletedCustomers ()
  {
    return doSelect (new Callable <List <ProcCustomer>> ()
    {
      public final List <ProcCustomer> call ()
      {
        return getEntityManager ().createQuery ("SELECT p FROM ProcCustomer p" + " WHERE p.deleted = true",
                                                ProcCustomer.class).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllDeletedCustomers ()
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (getEntityManager ().createQuery ("SELECT COUNT(p) FROM ProcCustomer p"
                                                                         + " WHERE p.deleted = true"));
      }
    }).get ().longValue ();
  }

  @Nullable
  public final ProcCustomer getActiveCustomerOfID (final int nCustomerID)
  {
    // Avoid query...
    if (nCustomerID < 0)
      return null;

    return doSelect (new Callable <ProcCustomer> ()
    {
      public final ProcCustomer call ()
      {
        final ProcCustomer aCustomer = getEntityManager ().find (ProcCustomer.class, Integer.valueOf (nCustomerID));
        return aCustomer == null || aCustomer.isDeleted () ? null : aCustomer;
      }
    }).get ();
  }

  @Nullable
  public final ProcCustomer getAnyCustomerOfID (final int nCustomerID)
  {
    // Avoid query...
    if (nCustomerID < 0)
      return null;

    return doSelect (new Callable <ProcCustomer> ()
    {
      public final ProcCustomer call ()
      {
        return getEntityManager ().find (ProcCustomer.class, Integer.valueOf (nCustomerID));
      }
    }).get ();
  }

  @Nonnull
  public final List <ProcCustomer> getAllMatchingCustomers (final String sSearchString)
  {
    final String sRealSearchString = '%' + sSearchString.replace ('%', ' ') + '%';
    return doSelect (new Callable <List <ProcCustomer>> ()
    {
      public final List <ProcCustomer> call ()
      {
        return getEntityManager ().createQuery ("SELECT p FROM ProcCustomer p"
                                                    + " WHERE p.deleted = false AND (p.entityID LIKE :search OR p.name LIKE :search)",
                                                ProcCustomer.class)
                                  .setParameter ("search", sRealSearchString)
                                  .getResultList ();
      }
    }).get ();
  }
}
