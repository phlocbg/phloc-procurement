package com.phloc.procurement.codelist;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum EResponseCodeName implements IHasDisplayText
{
  AA ("Belastungsanzeige", "Debit advice"),
  AC ("Bestätigung mit Details, Änderungen", "Acknowledge - with detail and change"),
  AD ("Bestätigung mit Details, keine Änderungen", "Acknowledge - with detail, no change,"),
  AF ("Belastungsanzeige / Bestätigung der Nachricht", "Debit advice/message acknowledgement"),
  AG ("Authentifizierung", "Authentication"),
  AJ ("In Arbeit", "Pending"),
  AP ("Akzeptiert", "Accepted"),
  AQ ("Antwort erwartet", "Response expected"),
  CA ("Akzeptiert, aber mit Bedingungen", "Conditionally accepted"),
  RE ("Abgelehnt", "Rejected");

  private final ITextProvider m_aTP;

  private EResponseCodeName (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}
