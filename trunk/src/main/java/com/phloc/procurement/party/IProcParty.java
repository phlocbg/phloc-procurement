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
package com.phloc.procurement.party;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.commons.IHasCountry;
import com.phloc.procurement.domain.IProcDeletableObject;
import com.phloc.procurement.idscheme.IEntityIDScheme;

/**
 * Interface for an abstract party domain object.
 * 
 * @author philip
 */
public interface IProcParty extends IProcDeletableObject, IHasCountry
{
  @Nullable
  IEntityIDScheme getEntityIDScheme ();

  @Nullable
  String getEntityID ();

  @Nullable
  String getEndpointID ();

  @Nullable
  String getName ();

  @Nullable
  String getStreet ();

  @Nullable
  String getBuildingNumber ();

  @Nullable
  String getPostbox ();

  @Nullable
  String getZipCode ();

  @Nullable
  String getCity ();

  @Nullable
  Locale getCountry ();

  @Nullable
  String getRegistrationName ();

  @Nullable
  String getCompanyRegistrationNumber ();

  @Nullable
  String getVATIN ();

  @Nullable
  String getBIC ();

  @Nullable
  String getIBAN ();
}
