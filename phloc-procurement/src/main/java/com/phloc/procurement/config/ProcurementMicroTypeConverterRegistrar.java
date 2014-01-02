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
package com.phloc.procurement.config;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;
import com.phloc.procurement.invoice.ProcInvoice;
import com.phloc.procurement.invoice.ProcInvoiceAttachment;
import com.phloc.procurement.invoice.ProcInvoiceAttachmentMicroTypeConverter;
import com.phloc.procurement.invoice.ProcInvoiceLine;
import com.phloc.procurement.invoice.ProcInvoiceLineMicroTypeConverter;
import com.phloc.procurement.invoice.ProcInvoiceMicroTypeConverter;
import com.phloc.procurement.order.ProcOrder;
import com.phloc.procurement.order.ProcOrderAttachment;
import com.phloc.procurement.order.ProcOrderAttachmentMicroTypeConverter;
import com.phloc.procurement.order.ProcOrderItem;
import com.phloc.procurement.order.ProcOrderItemMicroTypeConverter;
import com.phloc.procurement.order.ProcOrderMicroTypeConverter;
import com.phloc.procurement.party.ProcCustomer;
import com.phloc.procurement.party.ProcCustomerMicroTypeConverter;
import com.phloc.procurement.party.ProcCustomerPaymentTerm;
import com.phloc.procurement.party.ProcCustomerPaymentTermMicroTypeConverter;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.procurement.party.ProcMasterDataMicroTypeConverter;
import com.phloc.procurement.party.ProcSupplier;
import com.phloc.procurement.party.ProcSupplierMicroTypeConverter;

/**
 * This is the SPI implementation of the micro type converter registry for this
 * project.
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public class ProcurementMicroTypeConverterRegistrar implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    aRegistry.registerMicroElementTypeConverter (ProcInvoiceAttachment.class,
                                                 new ProcInvoiceAttachmentMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcInvoiceLine.class, new ProcInvoiceLineMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcInvoice.class, new ProcInvoiceMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcOrderAttachment.class,
                                                 new ProcOrderAttachmentMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcOrderItem.class, new ProcOrderItemMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcOrder.class, new ProcOrderMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcCustomerPaymentTerm.class,
                                                 new ProcCustomerPaymentTermMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcCustomer.class, new ProcCustomerMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcSupplier.class, new ProcSupplierMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ProcMasterData.class, new ProcMasterDataMicroTypeConverter ());
  }
}
