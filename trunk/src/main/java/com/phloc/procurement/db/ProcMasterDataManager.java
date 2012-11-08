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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.ESuccess;
import com.phloc.db.jpa.IEntityManagerProvider;
import com.phloc.procurement.party.ProcMasterData;

public class ProcMasterDataManager extends AbstractProcManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcMasterDataManager.class);

  public ProcMasterDataManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    super (aEntityMgrProvider);
  }

  @Nonnull
  public final ESuccess saveMasterData (@Nonnull final ProcMasterData aMasterData)
  {
    return doInTransaction (new Runnable ()
    {
      public final void run ()
      {
        getEntityManager ().merge (aMasterData);
        s_aLogger.info ("Masterdata saved: " + aMasterData);
      }
    });
  }

  @Nullable
  public final ProcMasterData getMasterData ()
  {
    return doSelect (new Callable <ProcMasterData> ()
    {
      public final ProcMasterData call ()
      {
        final List <ProcMasterData> aList = getEntityManager ().createQuery ("SELECT p FROM ProcMasterData p"
                                                                                 + " WHERE p.deleted = false",
                                                                             ProcMasterData.class).getResultList ();
        return aList == null || aList.size () != 1 ? null : aList.get (0);
      }
    }).get ();
  }
}
