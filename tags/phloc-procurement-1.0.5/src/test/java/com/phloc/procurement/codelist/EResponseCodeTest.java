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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link EResponseCode}.
 *
 * @author philip
 */
public final class EResponseCodeTest
{
  @Test
  public void testAll ()
  {
    for (final EResponseCode eResponseCode : EResponseCode.values ())
    {
      assertNotNull (eResponseCode.getID ());
      assertTrue (StringHelper.hasText (eResponseCode.getID ()));
      assertNotNull (eResponseCode.getDisplayText (Locale.GERMAN));
      assertSame (eResponseCode, EResponseCode.getFromIDOrNull (eResponseCode.getID ()));
    }
  }
}
