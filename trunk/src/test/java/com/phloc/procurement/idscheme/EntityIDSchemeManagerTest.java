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
package com.phloc.procurement.idscheme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.nonweb.mock.ScopeTestRule;

/**
 * Test class for class {@link EntityIDSchemeManager}.
 * 
 * @author philip
 */
public final class EntityIDSchemeManagerTest
{
  @Rule
  public final TestRule m_aScopeRule = new ScopeTestRule ();

  @Test
  public void testAPI ()
  {
    final EntityIDSchemeManager aMgr = EntityIDSchemeManager.getInstance ();
    for (final IEntityIDScheme eValue : aMgr.getAllRegisteredIDSchemes ())
    {
      assertNotNull (eValue.getID ());
      assertTrue (StringHelper.hasText (eValue.getID ()));
      assertSame (eValue, aMgr.getIDSchemeFromID (eValue.getID ()));
      assertNotNull (eValue.validate (null));
      assertTrue (eValue.validate (null).isInvalid ());
    }
  }

  @Test
  public void testValidate ()
  {
    // GLN
    assertTrue (EntityIDSchemeManager.GLN.validate ("1234567890123").isValid ());
    assertTrue (EntityIDSchemeManager.GLN.validate ("0000000000000").isValid ());
    assertFalse (EntityIDSchemeManager.GLN.validate ("123456789012a").isValid ());
    assertFalse (EntityIDSchemeManager.GLN.validate ("123456789012").isValid ());
    assertFalse (EntityIDSchemeManager.GLN.validate ("12345678901234").isValid ());

    // VATIN
    assertTrue (EntityIDSchemeManager.VATIN.validate ("ATU12345678").isValid ());
    assertFalse (EntityIDSchemeManager.VATIN.validate ("ATU1234567a").isValid ());
    assertTrue (EntityIDSchemeManager.VATIN.validate ("CY12345678A").isValid ());
    assertFalse (EntityIDSchemeManager.VATIN.validate ("CY12345678").isValid ());
    assertFalse (EntityIDSchemeManager.VATIN.validate ("CY123456789").isValid ());

    // DUNS
    assertTrue (EntityIDSchemeManager.DUNS.validate ("123456789").isValid ());
    assertTrue (EntityIDSchemeManager.DUNS.validate ("000000000").isValid ());
    assertFalse (EntityIDSchemeManager.DUNS.validate ("12345678a").isValid ());
    assertFalse (EntityIDSchemeManager.DUNS.validate ("12345678").isValid ());
    assertFalse (EntityIDSchemeManager.DUNS.validate ("1234567890").isValid ());

    // IBAN
    assertTrue (EntityIDSchemeManager.IBAN.validate ("DE89 3704 0044 0532 0130 00").isValid ());
    assertTrue (EntityIDSchemeManager.IBAN.validate ("DE89370400440532013000").isValid ());
    assertFalse (EntityIDSchemeManager.IBAN.validate ("DE89 3704 0044 0532 0130 01").isValid ());
    assertFalse (EntityIDSchemeManager.IBAN.validate ("any").isValid ());
  }
}
