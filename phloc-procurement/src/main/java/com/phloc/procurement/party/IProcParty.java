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
package com.phloc.procurement.party;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.commons.IHasCountry;
import com.phloc.procurement.domain.IProcDeletableObject;
import com.phloc.procurement.idscheme.IEntityIDScheme;

/**
 * Interface for an abstract party domain object.
 * 
 * @author Philip Helger
 */
public interface IProcParty extends IProcDeletableObject, IHasCountry
{
  /**
   * @return The entity ID scheme to be used (e.g. GLN or IBAN). May be
   *         <code>null</code>.
   */
  @Nullable
  IEntityIDScheme getEntityIDScheme ();

  /**
   * @return The ID matching the specified ID scheme.
   */
  @Nullable
  String getEntityID ();

  /**
   * @return The endpoint ID for document exchange. May be a URL or sthg. else.
   */
  @Nullable
  String getEndpointID ();

  /**
   * @return The name of the person or company.
   */
  @Nullable
  String getName ();

  /**
   * @return The street where the person resides.
   */
  @Nullable
  String getStreet ();

  /**
   * @return The building number to be used.
   */
  @Nullable
  String getBuildingNumber ();

  /**
   * @return The post box to be used.
   */
  @Nullable
  String getPostbox ();

  /**
   * @return The ZIP code of the city.
   */
  @Nullable
  String getZipCode ();

  /**
   * @return The name of the city.
   */
  @Nullable
  String getCity ();

  /**
   * @return The country the person lives in.
   */
  @Nullable
  Locale getCountry ();

  @Nullable
  String getRegistrationName ();

  @Nullable
  String getCompanyRegistrationNumber ();

  /**
   * @return The VAT identification number
   */
  @Nullable
  String getVATIN ();

  /**
   * @return Bank account BIC data.
   */
  @Nullable
  String getBIC ();

  /**
   * @return Bank account IBAN data.
   */
  @Nullable
  String getIBAN ();
}
