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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;

public final class HarmonizedSystemItem
{
  private final String m_sCode;
  private final IReadonlyMultiLingualText m_aTitle;

  public HarmonizedSystemItem (@Nonnull @Nonempty final String sCode, @Nonnull final IReadonlyMultiLingualText aTitle)
  {
    if (StringHelper.hasNoText (sCode))
      throw new IllegalArgumentException ("code");
    if (aTitle == null)
      throw new NullPointerException ("title");
    m_sCode = sCode;
    m_aTitle = aTitle;
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
}
