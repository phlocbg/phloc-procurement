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
package com.phloc.procurement.exchange.ubl;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Locale;

import oasis.names.specification.ubl.schema.xsd.order_2.OrderType;

import org.junit.Test;

import com.phloc.commons.locale.LocaleCache;
import com.phloc.procurement.exchange.AbstractExchangeTestCase;
import com.phloc.procurement.order.ProcOrderOutgoing;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.ubl.UBL20Writer;

/**
 * Test class for class {@link UBL20OrderSerializer}.
 * 
 * @author Philip Helger
 */
public final class UBL20OrderSerializerTest extends AbstractExchangeTestCase
{
  @Test
  public void testOrder ()
  {
    final Locale aLocale = LocaleCache.getLocale ("de", "AT");
    final ProcMasterData aMasterData = createMasterData ();
    final ProcOrderOutgoing aOrderO = new ProcOrderOutgoing ();
    aOrderO.setOrder (createOrder ());
    aOrderO.setSupplier (createSupplier ());

    // Convert to UBL 2.0 invoice domain object
    final OrderType aUBLOrder = UBL20OrderSerializer.writeOrder (aMasterData,
                                                                 aOrderO,
                                                                 aLocale,
                                                                 EUBLOrderCustomization.PEPPOL);
    assertNotNull (aUBLOrder);

    // Serialize to XML
    UBL20Writer.writeOrder (aUBLOrder, new File ("target/test-order.xml"));
  }
}
