package com.phloc.procurement.codelist;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum EDefaultUnitName implements IHasDisplayText
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
