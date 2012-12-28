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
package com.phloc.procurement.codelist;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasDisplayText;

/**
 * Possible response codes.<br>
 * See Technical documentation 6.3
 * 
 * @author philip
 */
public enum EResponseCode implements IResponseCode
{
  AA ("AA", EResponseCodeName.AA),
  AC ("AC", EResponseCodeName.AC),
  AD ("AD", EResponseCodeName.AD),
  AF ("AF", EResponseCodeName.AF),
  AG ("AG", EResponseCodeName.AG),
  AJ ("AJ", EResponseCodeName.AJ),
  AP ("AP", EResponseCodeName.AP),
  AQ ("AQ", EResponseCodeName.AQ),
  CA ("CA", EResponseCodeName.CA),
  RE ("RE", EResponseCodeName.RE);

  private final String m_sID;
  private final IHasDisplayText m_aName;

  private EResponseCode (@Nonnull @Nonempty final String sID, @Nonnull final EResponseCodeName eResponseCodeName)
  {
    m_sID = sID;
    m_aName = eResponseCodeName;
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

  @Nullable
  public static EResponseCode getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EResponseCode.class, sID);
  }
}
