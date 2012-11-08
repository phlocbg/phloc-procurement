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

import java.util.List;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.phloc.masterdata.currency.IReadonlyCurrencyValue;
import com.phloc.masterdata.trade.EIncoterm;
import com.phloc.procurement.domain.IHasProcAttachment;
import com.phloc.procurement.domain.IProcDeletableObject;

/**
 * Interface for an order domain object. This interface does not state anything
 * about the direction of the order (incoming or outgoing).
 * 
 * @author philip
 */
public interface IProcOrder extends IProcDeletableObject, IHasProcAttachment
{
  /**
   * @return The order number.
   */
  @Nullable
  String getOrderNumber ();

  /**
   * @return A list of all order items associated with this order. May be
   *         <code>null</code>.
   */
  @Nullable
  List <? extends IProcOrderItem> getOrderItems ();

  /**
   * @return The issue date of this order.
   */
  @Nullable
  LocalDate getIssueDate ();

  /**
   * @return The delivery start date.
   */
  @Nullable
  LocalDate getDeliveryStartDate ();

  /**
   * @return The delivery end date.
   */
  @Nullable
  LocalDate getDeliveryEndDate ();

  /**
   * @return Incoterms delivery terms.
   */
  @Nullable
  EIncoterm getIncoterm ();

  /**
   * @return The attachments of this object. May be <code>null</code>.
   */
  @Nullable
  List <? extends IProcOrderAttachment> getAttachments ();

  // Calculated fields

  /**
   * @return The calculated total net amount. May be <code>null</code> if no
   *         currency is defined.
   */
  @Nullable
  IReadonlyCurrencyValue getTotalNetAmount ();

  /**
   * @return The calculated total gross amount (net + tax). May be
   *         <code>null</code> if no currency is defined.
   */
  @Nullable
  IReadonlyCurrencyValue getTotalGrossAmount ();

  /**
   * @return The calculated total tax amount. May be <code>null</code> if no
   *         currency is defined.
   */
  @Nullable
  IReadonlyCurrencyValue getTotalTaxAmount ();
}
