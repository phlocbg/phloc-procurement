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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.ESuccess;
import com.phloc.db.jpa.IEntityManagerProvider;
import com.phloc.procurement.idscheme.IEntityIDScheme;
import com.phloc.procurement.party.ProcSupplier;

public class ProcSupplierManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcSupplierManager.class);

  public ProcSupplierManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ESuccess saveSupplier (@Nonnull final ProcSupplier aSupplier)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aSupplier);
        s_aLogger.info ("Supplier saved: " + aSupplier);
      }
    });
  }

  @Nonnull
  public final ESuccess deleteSupplier (@Nonnull final ProcSupplier aSupplier)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aSupplier.setDeleted (true);
        s_aLogger.info ("Supplier deleted: " + aSupplier);
      }
    });
  }

  @Nonnull
  public final ESuccess undeleteSupplier (@Nonnull final ProcSupplier aSupplier)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        aSupplier.setDeleted (false);
        s_aLogger.info ("Supplier undeleted: " + aSupplier);
      }
    });
  }

  @Nullable
  public final ProcSupplier getSupplierFromEntityID (final IEntityIDScheme aEntityIDScheme, final String sEntityID)
  {
    return doSelect (new Callable <ProcSupplier> ()
    {
      @Nullable
      public final ProcSupplier call ()
      {
        final List <ProcSupplier> aTmp = getEntityManager ().createQuery ("SELECT p FROM ProcSupplier p"
                                                                              + " WHERE p.deleted = false AND p.entityIDScheme = :scheme AND p.entityID = :value",
                                                                          ProcSupplier.class)
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
  public final List <ProcSupplier> getAllSuppliers ()
  {
    return doSelect (new Callable <List <ProcSupplier>> ()
    {
      public final List <ProcSupplier> call ()
      {
        return getEntityManager ().createQuery ("SELECT p FROM ProcSupplier p" + " WHERE p.deleted = false",
                                                ProcSupplier.class).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllSuppliers ()
  {
    return doInTransaction (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (getEntityManager ().createQuery ("SELECT COUNT(p) FROM ProcSupplier p"
                                                                         + " WHERE p.deleted = false"));
      }
    }).get ().longValue ();
  }

  @Nonnull
  public final List <ProcSupplier> getAllDeletedSuppliers ()
  {
    return doSelect (new Callable <List <ProcSupplier>> ()
    {
      public final List <ProcSupplier> call ()
      {
        return getEntityManager ().createQuery ("SELECT p FROM ProcSupplier p" + " WHERE p.deleted = true",
                                                ProcSupplier.class).getResultList ();
      }
    }).get ();
  }

  @Nonnegative
  public final long getCountAllDeletedSuppliers ()
  {
    return doSelect (new Callable <Number> ()
    {
      public final Number call ()
      {
        return getSelectCountResultObj (getEntityManager ().createQuery ("SELECT COUNT(p) FROM ProcSupplier p"
                                                                         + " WHERE p.deleted = true"));
      }
    }).get ().longValue ();
  }

  @Nullable
  public final ProcSupplier getActiveSupplierOfID (final int nSupplierID)
  {
    // Avoid query...
    if (nSupplierID < 0)
      return null;

    return doSelect (new Callable <ProcSupplier> ()
    {
      public final ProcSupplier call ()
      {
        final ProcSupplier aSupplier = getEntityManager ().find (ProcSupplier.class, Integer.valueOf (nSupplierID));
        return aSupplier == null || aSupplier.isDeleted () ? null : aSupplier;
      }
    }).get ();
  }

  @Nullable
  public final ProcSupplier getAnySupplierOfID (final int nSupplierID)
  {
    // Avoid query...
    if (nSupplierID < 0)
      return null;

    return doSelect (new Callable <ProcSupplier> ()
    {
      public final ProcSupplier call ()
      {
        return getEntityManager ().find (ProcSupplier.class, Integer.valueOf (nSupplierID));
      }
    }).get ();
  }
}
