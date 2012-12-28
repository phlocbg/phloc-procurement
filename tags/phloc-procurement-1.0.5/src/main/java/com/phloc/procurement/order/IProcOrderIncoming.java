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

import org.joda.time.DateTime;

import com.phloc.procurement.domain.IProcObjectWithClient;
import com.phloc.procurement.domain.IProcObjectWithState;
import com.phloc.procurement.party.IProcCustomer;

/**
 * Interface for an incoming order. This is for the use case where the current
 * entity is the seller (selling goods and services to customers).
 * 
 * @author philip
 */
public interface IProcOrderIncoming extends IHasProcOrder, IProcObjectWithState, IProcObjectWithClient
{
  /**
   * @return The customer from which the order was received.
   */
  @Nullable
  IProcCustomer getCustomer ();

  /**
   * @return The date and time when the incoming object was received.
   */
  @Nullable
  DateTime getReceiveDateTime ();
}
