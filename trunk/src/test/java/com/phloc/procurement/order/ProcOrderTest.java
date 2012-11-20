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
package com.phloc.procurement.order;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.procurement.attachment.ProcInMemoryAttachment;
import com.phloc.procurement.exchange.AbstractExchangeTestCase;

/**
 * Test class for class {@link ProcOrder}.
 * 
 * @author philip
 */
public final class ProcOrderTest extends AbstractExchangeTestCase
{
  @Test
  public void testOrder ()
  {
    final ProcOrder aOrder = createOrder ();
    assertNotNull (aOrder);
    final ProcOrderAttachment aAttach1 = new ProcOrderAttachment (aOrder,
                                                                  new ProcInMemoryAttachment ("attach1",
                                                                                              "Titel",
                                                                                              CMimeType.TEXT_PLAIN,
                                                                                              "abc".getBytes (CCharset.CHARSET_ISO_8859_1_OBJ)));
    aOrder.setAttachments (ContainerHelper.newList (aAttach1));
    PhlocTestUtils.testMicroTypeConversion (aOrder);
  }
}
