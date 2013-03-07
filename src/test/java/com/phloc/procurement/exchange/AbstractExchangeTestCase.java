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
package com.phloc.procurement.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.Rule;
import org.junit.rules.TestRule;

import com.phloc.commons.locale.country.CountryCache;
import com.phloc.datetime.PDTFactory;
import com.phloc.masterdata.currency.ECurrency;
import com.phloc.masterdata.price.ReadonlyPrice;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.masterdata.trade.EIncoterm;
import com.phloc.masterdata.vat.IVATItem;
import com.phloc.masterdata.vat.VATManager;
import com.phloc.procurement.codelist.ECommodityScheme;
import com.phloc.procurement.idscheme.EntityIDSchemeManager;
import com.phloc.procurement.invoice.ProcInvoice;
import com.phloc.procurement.invoice.ProcInvoiceLine;
import com.phloc.procurement.order.ProcOrder;
import com.phloc.procurement.order.ProcOrderItem;
import com.phloc.procurement.party.ProcCustomer;
import com.phloc.procurement.party.ProcCustomerPaymentTerm;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.procurement.party.ProcSupplier;
import com.phloc.scopes.mock.ScopeTestRule;

/**
 * Abstract test class for exchanging objects.
 * 
 * @author philip
 */
public abstract class AbstractExchangeTestCase
{
  @Rule
  public final TestRule m_aScopeRule = new ScopeTestRule ();

  private static final Locale COUNTRY = CountryCache.getCountry ("AT");

  @Nonnull
  public static final ProcMasterData createMasterData ()
  {
    final ProcMasterData aMasterData = new ProcMasterData ();
    aMasterData.setClient (123);
    aMasterData.setEntityIDScheme (EntityIDSchemeManager.GLN);
    aMasterData.setEntityID ("1234567890123");
    aMasterData.setEndpointID ("ep1234567890123");
    aMasterData.setName ("MasterDataName");
    aMasterData.setStreet ("My street");
    aMasterData.setBuildingNumber ("121/4");
    aMasterData.setPostbox ("1234567");
    aMasterData.setZipCode ("1140");
    aMasterData.setCity ("Wien");
    aMasterData.setCountry (COUNTRY);
    aMasterData.setRegistrationName ("masterdataRegistrationName");
    aMasterData.setCompanyRegistrationNumber ("masterdataCompanyRegistrationNumber");
    aMasterData.setVATIN ("masterdataVATIN");
    aMasterData.setBIC ("masterdataBIC");
    aMasterData.setIBAN ("masterdataIBAN");
    aMasterData.setFirstName ("master");
    aMasterData.setLastName ("date");
    aMasterData.setEmail ("ich@example.org");
    aMasterData.setTelephone ("+43/1/1234567");

    return aMasterData;
  }

  @Nonnull
  public static final ProcCustomer createCustomer ()
  {
    final ProcCustomer aCustomer = new ProcCustomer ();
    aCustomer.setClient (5);
    aCustomer.setEntityIDScheme (EntityIDSchemeManager.GLN);
    aCustomer.setEntityID ("1234567890123");
    aCustomer.setEndpointID ("ep1234567890123");
    aCustomer.setName ("CustomerName");
    aCustomer.setStreet ("My street");
    aCustomer.setBuildingNumber ("121/4");
    aCustomer.setPostbox ("1234567");
    aCustomer.setZipCode ("1140");
    aCustomer.setCity ("Wien");
    aCustomer.setCountry (COUNTRY);
    aCustomer.setRegistrationName ("customerRegistrationName");
    aCustomer.setCompanyRegistrationNumber ("customerCompanyRegistrationNumber");
    aCustomer.setVATIN ("customerVATIN");
    aCustomer.setBIC ("customerBIC");
    aCustomer.setIBAN ("customerIBAN");

    final List <ProcCustomerPaymentTerm> aPaymentTerms = new ArrayList <ProcCustomerPaymentTerm> ();
    final ProcCustomerPaymentTerm aPT1 = new ProcCustomerPaymentTerm (aCustomer);
    aPT1.setDays (10);
    aPT1.setDescription ("pt1Description");
    aPT1.setPercentage (1.23);
    aPaymentTerms.add (aPT1);
    final ProcCustomerPaymentTerm aPT2 = new ProcCustomerPaymentTerm (aCustomer);
    aPT2.setDays (20);
    aPT2.setDescription ("pt2Description");
    aPT2.setPercentage (0.5);
    aPaymentTerms.add (aPT2);
    aCustomer.setPaymentTerms (aPaymentTerms);

    return aCustomer;
  }

  @Nonnull
  public static final ProcSupplier createSupplier ()
  {
    final ProcSupplier aSupplier = new ProcSupplier ();
    aSupplier.setClient (123);
    aSupplier.setEntityIDScheme (EntityIDSchemeManager.GLN);
    aSupplier.setEntityID ("1234567890123");
    aSupplier.setEndpointID ("ep1234567890123");
    aSupplier.setName ("SupplierName");
    aSupplier.setStreet ("My street");
    aSupplier.setBuildingNumber ("121/4");
    aSupplier.setPostbox ("1234567");
    aSupplier.setZipCode ("1140");
    aSupplier.setCity ("Wien");
    aSupplier.setCountry (COUNTRY);
    aSupplier.setRegistrationName ("supplierRegistrationName");
    aSupplier.setCompanyRegistrationNumber ("supplierCompanyRegistrationNumber");
    aSupplier.setVATIN ("supplierVATIN");
    aSupplier.setBIC ("supplierBIC");
    aSupplier.setIBAN ("supplierIBAN");
    return aSupplier;
  }

  @Nonnull
  public static final ProcOrder createOrder ()
  {
    final ProcOrder aOrder = new ProcOrder ();
    aOrder.setOrderNumber ("orderNumber");
    aOrder.setIssueDate (PDTFactory.getCurrentLocalDate ());
    aOrder.setDeliveryStartDate (PDTFactory.getCurrentLocalDate ());
    aOrder.setDeliveryEndDate (PDTFactory.getCurrentLocalDate ().plusDays (30));
    aOrder.setIncoterm (EIncoterm.EXW);

    final List <ProcOrderItem> aOrderItems = new ArrayList <ProcOrderItem> ();
    final ProcOrderItem aItem1 = new ProcOrderItem (aOrder);
    aItem1.setDescription ("item1Description");
    aItem1.setNote ("item1Note");
    aItem1.setQuantity (10);
    aItem1.setUnitPrice (new ReadonlyPrice (ECurrency.EUR, new BigDecimal ("99.90"), VATManager.VATTYPE_NONE));
    aItem1.setCommodityScheme (ECommodityScheme.UNSPSC);
    aItem1.setCommodityID ("12345");
    aOrderItems.add (aItem1);
    aOrder.setOrderItems (aOrderItems);

    return aOrder;
  }

  @Nonnull
  public static final ProcInvoice createInvoice ()
  {
    final ProcInvoice aInvoice = new ProcInvoice ();
    aInvoice.setInvoiceNumber ("orderNumber");
    aInvoice.setIssueDate (PDTFactory.getCurrentLocalDate ());
    aInvoice.setInvoiceNote ("My invoice\nnote");

    final List <ProcInvoiceLine> aInvoiceLines = new ArrayList <ProcInvoiceLine> ();
    int nLineNo = 1;
    for (final Map.Entry <String, IVATItem> aEntry : VATManager.getDefaultInstance ()
                                                               .getAllVATItemsForCountry (COUNTRY)
                                                               .entrySet ())
    {
      final ProcInvoiceLine aLine = new ProcInvoiceLine (aInvoice);
      aLine.setOrderItemID ("orderItem" + nLineNo);
      aLine.setLineID ("line" + nLineNo);
      aLine.setCommodityScheme (ECommodityScheme.UNSPSC);
      aLine.setCommodityID (Integer.toString (12345 + nLineNo));
      aLine.setDescription ("itemDescription" + nLineNo);
      aLine.setQuantity (9 + nLineNo);
      aLine.setUnitPrice (new ReadonlyPrice (ECurrency.EUR,
                                             new BigDecimal ("99").add (BigDecimal.valueOf (nLineNo)),
                                             aEntry.getValue ()));
      aLine.setTaxCategory (ETaxCategoryUN5305.C);
      aLine.setRemark ("My remark " + nLineNo);
      aInvoiceLines.add (aLine);
      ++nLineNo;
    }
    aInvoice.setInvoiceLines (aInvoiceLines);

    return aInvoice;
  }
}
