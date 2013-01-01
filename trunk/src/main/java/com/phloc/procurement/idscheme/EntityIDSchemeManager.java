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
package com.phloc.procurement.idscheme;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.masterdata.swift.validation.StringIBANValidator;
import com.phloc.masterdata.vat.validation.StringVATINValidator;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;
import com.phloc.validation.validator.string.StringRegExValidator;

/**
 * Manages all available entity ID schemes.
 * 
 * @author philip
 */
@ThreadSafe
public final class EntityIDSchemeManager extends GlobalSingleton
{
  @Translatable
  private static enum EText implements IHasDisplayText
  {
    NAME_GLN ("Global Location Number", "Global Location Number"),
    NAME_VATIN ("Umsatzsteueridentifikationsnummer", "Value added tax identification number"),
    NAME_DUNS ("Dun and Bradstreet Number", "Dun and Bradstreet Number"),
    NAME_IBAN ("International Bank Account Number", "International Bank Account Number"),
    VALIDATE_GLN ("Der Wert muss aus 13 Zahlen bestehen.", "The value must be assembled from 13 digits."),
    VALIDATE_DUNS ("Der Wert muss aus 9 Zahlen bestehen.", "The value must be assembled from 9 digits.");

    private final ITextProvider m_aTP;

    private EText (@Nonnull final String sDE, @Nonnull final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    public String getDisplayText (final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  // Constant entity ID schemes!
  // Important: don't change these IDs, as they reflect the old enum indices
  public static final EntityIDScheme GLN = new EntityIDScheme ("0",
                                                               "GLN",
                                                               EText.NAME_GLN,
                                                               new StringRegExValidator ("^[0-9]{13}$",
                                                                                         EText.VALIDATE_GLN));
  public static final EntityIDScheme VATIN = new EntityIDScheme ("1",
                                                                 "VATIN",
                                                                 EText.NAME_VATIN,
                                                                 new StringVATINValidator ());
  public static final EntityIDScheme DUNS = new EntityIDScheme ("2",
                                                                "DUNS",
                                                                EText.NAME_DUNS,
                                                                new StringRegExValidator ("^[0-9]{9}$",
                                                                                          EText.VALIDATE_DUNS));
  public static final EntityIDScheme IBAN = new EntityIDScheme ("3",
                                                                "IBAN",
                                                                EText.NAME_IBAN,
                                                                new StringIBANValidator ());

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, EntityIDScheme> m_aSchemes = new HashMap <String, EntityIDScheme> ();

  @Deprecated
  @UsedViaReflection
  public EntityIDSchemeManager ()
  {
    // Register default entity ID schemes
    addIDScheme (GLN);
    addIDScheme (VATIN);
    addIDScheme (DUNS);
    addIDScheme (IBAN);
  }

  public static EntityIDSchemeManager getInstance ()
  {
    return getGlobalSingleton (EntityIDSchemeManager.class);
  }

  public void addIDScheme (@Nonnull final EntityIDScheme aIDScheme)
  {
    if (aIDScheme == null)
      throw new NullPointerException ("IDScheme");

    m_aRWLock.writeLock ().lock ();
    try
    {
      final String sKey = aIDScheme.getID ();
      if (m_aSchemes.containsKey (sKey))
        throw new IllegalArgumentException ("An ID scheme with the ID " + sKey + " is already registerd!");
      m_aSchemes.put (sKey, aIDScheme);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void updateIDScheme (@Nonnull final EntityIDScheme aIDScheme)
  {
    if (aIDScheme == null)
      throw new NullPointerException ("IDScheme");

    m_aRWLock.writeLock ().lock ();
    try
    {
      final String sKey = aIDScheme.getID ();
      if (!m_aSchemes.containsKey (sKey))
        throw new IllegalArgumentException ("An ID scheme with the ID " + sKey + " is not yet registerd!");
      m_aSchemes.put (sKey, aIDScheme);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public EntityIDScheme getIDSchemeFromID (@Nullable final String sID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aSchemes.get (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <? extends IEntityIDScheme> getAllRegisteredIDSchemes ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aSchemes.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
