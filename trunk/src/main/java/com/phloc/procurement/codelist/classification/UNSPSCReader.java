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
package com.phloc.procurement.codelist.classification;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.MultiLingualText;

@Immutable
public final class UNSPSCReader
{
  public static final String ELEMENT_ROOT = "unspsc";
  public static final String ELEMENT_ITEM = "item";
  public static final String ATTR_CODE = "code";
  public static final String ATTR_KEY = "key";
  public static final String ELEMENT_TITLE = "title";
  public static final String ELEMENT_DEFINITION = "definition";

  private UNSPSCReader ()
  {}

  public static void readItems (@Nonnull final IReadableResource aRes,
                                @Nonnull final INonThrowingRunnableWithParameter <UNSPSCItem> aCallback)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aRes);
    if (aDoc != null)
    {
      for (final IMicroElement eItem : aDoc.getDocumentElement ().getAllChildElements (ELEMENT_ITEM))
      {
        final String sCode = eItem.getAttribute (ATTR_CODE);
        final String sKey = eItem.getAttribute (ATTR_KEY);
        final IMicroElement eTitle = eItem.getFirstChildElement (ELEMENT_TITLE);
        final IReadonlyMultiLingualText aTitle = MicroTypeConverter.convertToNative (eTitle, MultiLingualText.class);
        final IMicroElement eDefinition = eItem.getFirstChildElement (ELEMENT_DEFINITION);
        final IReadonlyMultiLingualText aDefinition = MicroTypeConverter.convertToNative (eDefinition,
                                                                                          MultiLingualText.class);
        aCallback.run (new UNSPSCItem (sKey, sCode, aTitle, aDefinition));
      }
    }
  }
}
