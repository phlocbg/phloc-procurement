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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.validation.result.IValidationResult;
import com.phloc.validation.validator.IStringValidator;

/**
 * Default implementation of the {@link IEntityIDScheme} interface.
 * 
 * @author Philip Helger
 */
@Immutable
public final class EntityIDScheme implements IEntityIDScheme
{
  private final String m_sID;
  private final String m_sShortName;
  private final IHasDisplayText m_aDisplayName;
  private final IStringValidator m_aValidator;

  public EntityIDScheme (@Nonnull @Nonempty final String sID,
                         @Nonnull @Nonempty final String sShortName,
                         @Nonnull final IHasDisplayText aDisplayName,
                         @Nonnull final IStringValidator aValidator)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID may not be empty");
    if (StringHelper.hasNoText (sShortName))
      throw new IllegalArgumentException ("shortName may not be empty");
    if (aDisplayName == null)
      throw new NullPointerException ("displayName");
    if (aValidator == null)
      throw new NullPointerException ("validator");
    m_sID = sID;
    m_sShortName = sShortName;
    m_aDisplayName = aDisplayName;
    m_aValidator = aValidator;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getShortName ()
  {
    return m_sShortName;
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aDisplayName.getDisplayText (aContentLocale);
  }

  @Nonnull
  public IValidationResult validate (@Nullable final String sValue)
  {
    return m_aValidator.validate (sValue);
  }

  @Nonnull
  public EntityIDScheme getCopyWithDifferentValidator (@Nonnull final IStringValidator aValidator)
  {
    return new EntityIDScheme (m_sID, m_sShortName, m_aDisplayName, aValidator);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof EntityIDScheme))
      return false;
    final EntityIDScheme rhs = (EntityIDScheme) o;
    return m_sID.equals (rhs.m_sID) && m_sShortName.equals (rhs.m_sShortName) && m_aValidator.equals (rhs.m_aValidator);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID).append (m_sShortName).append (m_aValidator).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("shortName", m_sShortName)
                                       .append ("displayName", m_aDisplayName)
                                       .append ("validator", m_aValidator)
                                       .toString ();
  }
}
