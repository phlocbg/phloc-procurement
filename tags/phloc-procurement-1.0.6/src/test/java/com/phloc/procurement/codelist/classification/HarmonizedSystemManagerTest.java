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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.scopes.nonweb.mock.ScopeTestRule;

/**
 * Test class for class {@link HarmonizedSystemManager}.
 * 
 * @author philip
 */
public final class HarmonizedSystemManagerTest
{
  @Rule
  public final TestRule m_aScopeRule = new ScopeTestRule ();

  @Test
  public void testInit ()
  {
    assertNotNull (HarmonizedSystemManager.getInstance ());
    assertNotNull (HarmonizedSystemManager.getInstance ().getItemOfCode ("010110"));
    assertNull (HarmonizedSystemManager.getInstance ().getItemOfCode ("010110X"));
  }
}
