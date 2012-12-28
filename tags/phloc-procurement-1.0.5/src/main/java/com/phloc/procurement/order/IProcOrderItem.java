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
package com.phloc.procurement.order;

import javax.annotation.Nullable;

import com.phloc.masterdata.currency.IReadonlyCurrencyValue;
import com.phloc.masterdata.price.Price;
import com.phloc.procurement.codelist.ICommodityScheme;
import com.phloc.procurement.domain.IProcObject;

public interface IProcOrderItem extends IProcObject
{
  @Nullable
  String getDescription ();

  @Nullable
  String getNote ();

  int getQuantity ();

  @Nullable
  Price getUnitPrice ();

  /**
   * @return The commodity scheme for this order line.
   */
  @Nullable
  ICommodityScheme getCommodityScheme ();

  /**
   * @return The ID of the commodity scheme for this order line.
   */
  @Nullable
  String getCommoditySchemeID ();

  /**
   * @return The ID within the selected commodity scheme.
   */
  @Nullable
  String getCommodityID ();

  // Calculated fields:

  @Nullable
  IReadonlyCurrencyValue getTotalNetAmount ();

  @Nullable
  IReadonlyCurrencyValue getTotalGrossAmount ();

  @Nullable
  IReadonlyCurrencyValue getTotalTaxAmount ();
}
