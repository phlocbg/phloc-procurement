package com.phloc.procurement.codelist;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum ECommoditySchemeName implements IHasDisplayText
{
  UNSPSC ("UNSPSC", "UNSPSC"),
  ECLASS ("e-Cl@ss", "e-Cl@ss"),
  HS ("Harmonized System", "Harmonized System"),
  ECROKAT ("eCROKAT", "eCROKAT");

  private final ITextProvider m_aTP;

  private ECommoditySchemeName (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}
