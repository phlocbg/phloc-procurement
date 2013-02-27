/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.procurement.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.procurement.invoice.ProcInvoice;
import com.phloc.procurement.order.ProcOrder;
import com.phloc.procurement.party.ProcCustomer;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.procurement.party.ProcSupplier;

public enum EProcDomainObjectType implements IHasID <String>
{
  MASTERDATA ("masterdata", ProcMasterData.class),
  CUSTOMER ("customer", ProcCustomer.class),
  SUPPLIER ("supplier", ProcSupplier.class),
  ORDER ("order", ProcOrder.class),
  INVOICE ("invoice", ProcInvoice.class);

  private final String m_sID;
  private final Class <?> m_aImplClass;

  private EProcDomainObjectType (@Nonnull @Nonempty final String sID, @Nonnull final Class <?> aImplClass)
  {
    m_sID = sID;
    m_aImplClass = aImplClass;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public Class <?> getImplClass ()
  {
    return m_aImplClass;
  }

  @Nullable
  public static EProcDomainObjectType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EProcDomainObjectType.class, sID);
  }
}
