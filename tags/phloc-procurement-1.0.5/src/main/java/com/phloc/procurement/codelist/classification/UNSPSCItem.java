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
package com.phloc.procurement.codelist.classification;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;

public final class UNSPSCItem
{
  private final String m_sKey;
  private final String m_sCode;
  private final IReadonlyMultiLingualText m_aTitle;
  private final IReadonlyMultiLingualText m_aDefinition;

  public UNSPSCItem (@Nonnull @Nonempty final String sKey,
                     @Nonnull @Nonempty final String sCode,
                     @Nonnull final IReadonlyMultiLingualText aTitle,
                     @Nullable final IReadonlyMultiLingualText aDefinition)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key");
    if (StringHelper.hasNoText (sCode))
      throw new IllegalArgumentException ("code");
    if (aTitle == null)
      throw new NullPointerException ("title");
    m_sKey = sKey;
    m_sCode = sCode;
    m_aTitle = aTitle;
    m_aDefinition = aDefinition;
  }

  @Nonnull
  @Nonempty
  public String getKey ()
  {
    return m_sKey;
  }

  @Nonnull
  @Nonempty
  public String getCode ()
  {
    return m_sCode;
  }

  @Nonnull
  public IReadonlyMultiLingualText getTitle ()
  {
    return m_aTitle;
  }

  @Nullable
  public IReadonlyMultiLingualText getDefinition ()
  {
    return m_aDefinition;
  }
}
