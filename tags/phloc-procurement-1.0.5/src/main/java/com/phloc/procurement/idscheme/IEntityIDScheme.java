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
package com.phloc.procurement.idscheme;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.validation.validator.IStringValidator;

/**
 * Interface for a single entity ID scheme. It has an ID, a locale dependent
 * display text, a locale independent short name and a validator to validate
 * values.
 * 
 * @author philip
 */
public interface IEntityIDScheme extends IHasID <String>, IHasDisplayText, IStringValidator
{
  /**
   * @return The shortcut name of the scheme (e.g. "IBAN" for the International
   *         Bank Account Number). Language independent!
   */
  @Nonnull
  @Nonempty
  String getShortName ();
}
