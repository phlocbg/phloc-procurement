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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;

@Immutable
public final class HarmonizedSystemManager extends GlobalSingleton
{
  /**
   * The locale to be used in case a title is not available in the desired
   * language
   */
  public static final Locale FALLBACK_LOCALE = Locale.ENGLISH;
  private final Map <String, HarmonizedSystemItem> m_aMap = new LinkedHashMap <String, HarmonizedSystemItem> ();

  @UsedViaReflection
  @Deprecated
  public HarmonizedSystemManager ()
  {
    HarmonizedSystemReader.readItems (new ClassPathResource ("codelists/harmonized_system.xml"),
                                      new INonThrowingRunnableWithParameter <HarmonizedSystemItem> ()
                                      {
                                        public void run (@Nullable final HarmonizedSystemItem aItem)
                                        {
                                          if (aItem != null)
                                            m_aMap.put (aItem.getCode (), aItem);
                                        }
                                      });
  }

  @Nonnull
  public static HarmonizedSystemManager getInstance ()
  {
    return getGlobalSingleton (HarmonizedSystemManager.class);
  }

  @Nullable
  public HarmonizedSystemItem getItemOfCode (@Nullable final String sCode)
  {
    return m_aMap.get (sCode);
  }

  @Nonnull
  public Iterator <HarmonizedSystemItem> getItemIterator ()
  {
    return m_aMap.values ().iterator ();
  }
}
