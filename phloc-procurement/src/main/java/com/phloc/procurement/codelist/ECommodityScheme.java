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
package com.phloc.procurement.codelist;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.validation.result.IValidationResult;
import com.phloc.validation.validator.IStringValidator;
import com.phloc.validation.validator.string.StringRegExValidator;

/**
 * Commodity scheme.<br>
 * See Technical documentation 6.8
 * 
 * @author Philip Helger
 */
public enum ECommodityScheme implements ICommodityScheme
{
  UNSPSC ("UNSPSC", ECommoditySchemeName.UNSPSC, new StringRegExValidator ("^[0-9]{10}$")),
  ECLASS ("ECLASS", ECommoditySchemeName.ECLASS, new StringRegExValidator ("^.{8}$")),
  HS ("HS", ECommoditySchemeName.HS, new StringRegExValidator ("^[0-9]{10}$")),
  // FIXME the validation scheme for ECROKAT is still missing!
  ECROKAT ("ECROKAT", ECommoditySchemeName.ECROKAT, new StringRegExValidator ("^.+$"));
  private final String m_sID;
  private final IHasDisplayText m_aName;
  private final IStringValidator m_aValidator;

  private ECommodityScheme (@Nonnull @Nonempty final String sID,
                            @Nonnull final ECommoditySchemeName eCommoditySchemeName,
                            @Nonnull final IStringValidator aValidator)
  {
    m_sID = sID;
    m_aName = eCommoditySchemeName;
    m_aValidator = aValidator;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aName.getDisplayText (aContentLocale);
  }

  @Nonnull
  public IValidationResult validate (@Nullable final String sValue)
  {
    return m_aValidator.validate (sValue);
  }

  @Nullable
  public static ECommodityScheme getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (ECommodityScheme.class, sID);
  }
}
