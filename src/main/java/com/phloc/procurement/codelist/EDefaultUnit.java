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
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
enum EDefaultUnitName implements IHasDisplayText
{
  BE ("Bündel ", "Bundle"),
  BG ("Beutel", "Bag"),
  BX ("Kisten", "Box"),
  CH ("Container", "Container"),
  CMT ("Zentimeter", "Centimetre"),
  E49 ("Personentage ", "working day"),
  GLI ("Gallone (UK)", "gallon (UK)"),
  GLL ("Gallone (US)", "gallon (US)"),
  GRM ("Gramm", "Gram"),
  GT ("Bruttotonne ", "gross ton"),
  H87 ("Stück", "Piece"),
  HDW ("hundert Kilogramm Trockengewicht", "hundred kilogram, dry weight"),
  HLT ("Hektoliter", "Hectolitre"),
  J57 ("Barrel (UK)", "barrel (UK petroleum)"),
  KGM ("Kilogramm", "Kilogram"),
  L61 ("Pinte (US dry)", "pint (US dry)"),
  L84 ("Tonne (UK Versand)", "ton (UK shipping)"),
  L86 ("Tonne (US-Express)", "ton (US shipping)"),
  LBR ("Pfund", "Pound"),
  LTR ("Liter", "Litre"),
  MTK ("Quadratmeter", "square metre"),
  MTQ ("Kubikmeter", "cubic metre"),
  MTR ("Meter", "Metre"),
  NC ("Auto", "Car"),
  NPL ("Anzahl Pakete ", "number of parcels"),
  NPR ("Anzahl Paare", "number of pairs"),
  NPT ("Stück", "number of parts"),
  NV ("Fahrzeuge", "Vehicle"),
  RA ("Rack", "Rack"),
  ST ("Blatt", "Sheet"),
  STL ("Standard-Liter-", "standard litre"),
  STN ("ton (USA) oder short ton (UK / US)", "ton (US) or short ton (UK/US)"),
  T3 ("tausend Stück ", "thousand piece"),
  TNE ("metrische Tonne ", "tonne (metric ton)"),
  TV ("tausend Kilogramm", "thousand kilogram"),
  ZZ ("gemeinsam definiert", "mutually defined");

  private final ITextProvider m_aTP;

  private EDefaultUnitName (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}

/**
 * Possible units of measurement.<br>
 * See Technical documentation 6.6
 * 
 * @author philip
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
