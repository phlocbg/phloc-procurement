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

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;

import com.phloc.db.jpa.AbstractJPAEnabledManager;
import com.phloc.db.jpa.IEntityManagerProvider;

/**
 * Abstract base class for entity managers. Provides the
 * {@link #doInTransaction(Runnable)} method and other sanity methods.
 * 
 * @author philip
 */
public abstract class AbstractProcManager extends AbstractJPAEnabledManager
{
  private final IEntityManagerProvider m_aHasEntityProvider;

  protected AbstractProcManager (@Nonnull final IEntityManagerProvider aEntityMgrProvider)
  {
    if (aEntityMgrProvider == null)
      throw new NullPointerException ("hasEntityMgr");
    m_aHasEntityProvider = aEntityMgrProvider;
  }

  @Override
  protected final EntityManager getEntityManager ()
  {
    return m_aHasEntityProvider.getEntityManager ();
  }
}
