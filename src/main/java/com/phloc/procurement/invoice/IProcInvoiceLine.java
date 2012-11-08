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
package com.phloc.procurement.invoice;

import javax.annotation.Nullable;

import com.phloc.masterdata.currency.IReadonlyCurrencyValue;
import com.phloc.masterdata.price.Price;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.procurement.codelist.ICommodityScheme;
import com.phloc.procurement.domain.IProcObject;

/**
 * Read-only interface for a single invoice line
 * 
 * @author philip
 */
public interface IProcInvoiceLine extends IProcObject
{
  /**
   * @return The ID of the referenced order item
   */
  @Nullable
  String getOrderItemID ();

  /**
   * @return The ID of this invoice line
   */
  @Nullable
  String getLineID ();

  // FIXME: add supplier classification

  /**
   * @return The commodity scheme for this invoice line.
   */
  @Nullable
  ICommodityScheme getCommodityScheme ();

  /**
   * @return The ID within the selected commodity scheme.
   */
  @Nullable
  String getCommodityID ();

  // preset with order item description
  @Nullable
  String getDescription ();

  // preset with order item quantity
  int getQuantity ();

  // preset with order item price
  @Nullable
  Price getUnitPrice ();

  @Nullable
  ETaxCategoryUN5305 getTaxCategory ();

  @Nullable
  String getRemark ();

  // Calculated fields:

  @Nullable
  IReadonlyCurrencyValue getTotalNetAmount ();

  @Nullable
  IReadonlyCurrencyValue getTotalGrossAmount ();

  @Nullable
  IReadonlyCurrencyValue getTotalTaxAmount ();
}
