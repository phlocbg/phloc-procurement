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
package com.phloc.procurement.exchange;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.procurement.invoice.ProcInvoice;
import com.phloc.procurement.order.ProcOrder;
import com.phloc.procurement.party.ProcCustomer;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.procurement.party.ProcSupplier;

/**
 * Test class for class {@link SimpleProcSerializer}.
 * 
 * @author philip
 */
public final class SimpleProcSerializerTest extends AbstractExchangeTestCase
{
  @Test
  public void testMasterData ()
  {
    ProcMasterData aMasterData = new ProcMasterData ();
    IMicroElement eMasterData = SimpleProcSerializer.writeMasterData (aMasterData);
    assertNotNull (eMasterData);
    ProcMasterData aMasterData2 = SimpleProcSerializer.readMasterData (eMasterData, true);
    assertNotNull (aMasterData2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aMasterData, aMasterData2);

    aMasterData = createMasterData ();
    eMasterData = SimpleProcSerializer.writeMasterData (aMasterData);
    assertNotNull (eMasterData);
    aMasterData2 = SimpleProcSerializer.readMasterData (eMasterData, true);
    assertNotNull (aMasterData2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aMasterData, aMasterData2);
  }

  @Test
  public void testCustomer ()
  {
    ProcCustomer aCustomer = new ProcCustomer ();
    IMicroElement eCustomer = SimpleProcSerializer.writeCustomer (aCustomer);
    assertNotNull (eCustomer);
    ProcCustomer aCustomer2 = SimpleProcSerializer.readCustomer (eCustomer, true);
    assertNotNull (aCustomer2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aCustomer, aCustomer2);

    aCustomer = createCustomer ();
    eCustomer = SimpleProcSerializer.writeCustomer (aCustomer);
    assertNotNull (eCustomer);
    aCustomer2 = SimpleProcSerializer.readCustomer (eCustomer, true);
    assertNotNull (aCustomer2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aCustomer, aCustomer2);
  }

  @Test
  public void testSupplier ()
  {
    ProcSupplier aSupplier = new ProcSupplier ();
    IMicroElement eSupplier = SimpleProcSerializer.writeSupplier (aSupplier);
    assertNotNull (eSupplier);
    ProcSupplier aSupplier2 = SimpleProcSerializer.readSupplier (eSupplier, true);
    assertNotNull (aSupplier2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aSupplier, aSupplier2);

    aSupplier = createSupplier ();
    eSupplier = SimpleProcSerializer.writeSupplier (aSupplier);
    assertNotNull (eSupplier);
    aSupplier2 = SimpleProcSerializer.readSupplier (eSupplier, true);
    assertNotNull (aSupplier2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aSupplier, aSupplier2);
  }

  @Test
  public void testOrder ()
  {
    ProcOrder aOrder = new ProcOrder ();
    IMicroElement eOrder = SimpleProcSerializer.writeOrder (aOrder);
    assertNotNull (eOrder);
    ProcOrder aOrder2 = SimpleProcSerializer.readOrder (eOrder, new ProcAttachmentReadHandlerDoNothing (), true);
    assertNotNull (aOrder2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aOrder, aOrder2);

    aOrder = createOrder ();
    eOrder = SimpleProcSerializer.writeOrder (aOrder);
    assertNotNull (eOrder);
    aOrder2 = SimpleProcSerializer.readOrder (eOrder, new ProcAttachmentReadHandlerDoNothing (), true);
    assertNotNull (aOrder2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aOrder, aOrder2);
  }

  @Test
  public void testInvoice ()
  {
    ProcInvoice aInvoice = new ProcInvoice ();
    IMicroElement eInvoice = SimpleProcSerializer.writeInvoice (aInvoice);
    assertNotNull (eInvoice);
    ProcInvoice aInvoice2 = SimpleProcSerializer.readInvoice (eInvoice, new ProcAttachmentReadHandlerDoNothing (), true);
    assertNotNull (aInvoice2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aInvoice, aInvoice2);

    aInvoice = createInvoice ();
    eInvoice = SimpleProcSerializer.writeInvoice (aInvoice);
    assertNotNull (eInvoice);
    aInvoice2 = SimpleProcSerializer.readInvoice (eInvoice, new ProcAttachmentReadHandlerDoNothing (), true);
    assertNotNull (aInvoice2);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aInvoice, aInvoice2);
  }
}
