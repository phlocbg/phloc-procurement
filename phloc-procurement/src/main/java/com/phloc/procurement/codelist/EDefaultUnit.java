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
package com.phloc.procurement.codelist;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasDisplayText;

/**
 * Possible units of measurement.<br>
 * See Technical documentation 6.6
 * 
 * @author Philip Helger
 */
public enum EDefaultUnit implements IDefaultUnit
{
  BE ("BE", EDefaultUnitName.BE),
  BG ("BG", EDefaultUnitName.BG),
  BX ("BX", EDefaultUnitName.BX),
  CH ("CH", EDefaultUnitName.CH),
  CMT ("CMT", EDefaultUnitName.CMT),
  E49 ("E49", EDefaultUnitName.E49),
  GLI ("GLI", EDefaultUnitName.GLI),
  GLL ("GLL", EDefaultUnitName.GLL),
  GRM ("GRM", EDefaultUnitName.GRM),
  GT ("GT", EDefaultUnitName.GT),
  H87 ("H87", EDefaultUnitName.H87),
  HDW ("HDW", EDefaultUnitName.HDW),
  HLT ("HLT", EDefaultUnitName.HLT),
  J57 ("J57", EDefaultUnitName.J57),
  KGM ("KGM", EDefaultUnitName.KGM),
  L61 ("L61", EDefaultUnitName.L61),
  L84 ("L84", EDefaultUnitName.L84),
  L86 ("L86", EDefaultUnitName.L86),
  LBR ("LBR", EDefaultUnitName.LBR),
  LTR ("LTR", EDefaultUnitName.LTR),
  MTK ("MTK", EDefaultUnitName.MTK),
  MTQ ("MTQ", EDefaultUnitName.MTQ),
  MTR ("MTR", EDefaultUnitName.MTR),
  NC ("NC", EDefaultUnitName.NC),
  NPL ("NPL", EDefaultUnitName.NPL),
  NPR ("NPR", EDefaultUnitName.NPR),
  NPT ("NPT", EDefaultUnitName.NPT),
  NV ("NV", EDefaultUnitName.NV),
  RA ("RA", EDefaultUnitName.RA),
  ST ("ST", EDefaultUnitName.ST),
  STL ("STL", EDefaultUnitName.STL),
  STN ("STN", EDefaultUnitName.STN),
  T3 ("T3", EDefaultUnitName.T3),
  TNE ("TNE", EDefaultUnitName.TNE),
  TV ("TV", EDefaultUnitName.TV),
  ZZ ("ZZ", EDefaultUnitName.ZZ);

  private final String m_sID;
  private final IHasDisplayText m_aName;

  private EDefaultUnit (@Nonnull @Nonempty final String sID, @Nonnull final EDefaultUnitName eUnitName)
  {
    m_sID = sID;
    m_aName = eUnitName;
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
  public static EDefaultUnit getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EDefaultUnit.class, sID);
  }
}
