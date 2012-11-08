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
package com.phloc.procurement.exchange.ubl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BranchType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CommodityClassificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.FinancialAccountType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.FinancialInstitutionType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.LineItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.OrderLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentMeansType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BaseQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CommodityCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentChannelCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentDueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentMeansCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ProfileIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.QuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.UBLVersionIDType;
import oasis.names.specification.ubl.schema.xsd.order_2.OrderType;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;
import un.unece.uncefact.codelist.specification._66411._2001.UnitCodeContentType;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.datetime.xml.PDTXMLConverter;
import com.phloc.masterdata.currency.ECurrency;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.masterdata.tax.ETaxTypeUN5153;
import com.phloc.procurement.order.ProcOrder;
import com.phloc.procurement.order.ProcOrderItem;
import com.phloc.procurement.order.ProcOrderOutgoing;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.procurement.party.ProcSupplier;

@Immutable
public final class UBL20OrderSerializer extends AbstractUBL20Serializer
{
  @Nonnull
  public static OrderType writeOrder (@Nonnull final ProcMasterData aSender,
                                      @Nonnull final ProcOrderOutgoing aOrderO,
                                      @Nonnull final Locale aContentLocale,
                                      @Nonnull final EUBLOrderCustomization eCustomization)
  {
    if (aSender == null)
      throw new NullPointerException ("sender");
    if (aOrderO == null)
      throw new NullPointerException ("invoiceOutgoing");
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    if (aOrderO.getSupplier () == null ||
        aOrderO.getOrder () == null ||
        ContainerHelper.isEmpty (aOrderO.getOrder ().getOrderItems ()))
      throw new IllegalArgumentException ("Cannot convert the passed invoice to UBL!");

    final ProcSupplier aSupplier = aOrderO.getSupplier ();
    final ProcOrder aOrder = aOrderO.getOrder ();
    final ECurrency eCurrency = aOrder.getOrderItems ().get (0).getUnitPrice ().getCurrency ();
    final CurrencyCodeContentType eUBLCurrency = CurrencyCodeContentType.fromValue (eCurrency.getID ());

    final OrderType ret = new OrderType ();

    // Header
    {
      // version
      final UBLVersionIDType aVersion = new UBLVersionIDType ();
      aVersion.setValue ("2.0");
      ret.setUBLVersionID (aVersion);

      // customization
      final CustomizationIDType aCustomization = new CustomizationIDType ();
      aCustomization.setSchemeID (eCustomization.getCustomizationSchemeID ());
      aCustomization.setValue (eCustomization.getCustomizationValue ());
      ret.setCustomizationID (aCustomization);

      // profile
      if (StringHelper.hasText (eCustomization.getProfileID ()))
      {
        final ProfileIDType aProfile = new ProfileIDType ();
        aProfile.setValue (eCustomization.getProfileID ());
        ret.setProfileID (aProfile);
      }

      // ID
      final IDType aID = new IDType ();
      aID.setValue (Integer.toString (aOrderO.getID ()));
      ret.setID (aID);

      if (aOrder.getIssueDate () != null)
      {
        // Issue date
        final IssueDateType aIssueDate = new IssueDateType ();
        aIssueDate.setValue (PDTXMLConverter.getXMLCalendarDate (aOrder.getIssueDate ()));
        ret.setIssueDate (aIssueDate);
      }

      // Document currency
      final DocumentCurrencyCodeType aDocumentCurrency = new DocumentCurrencyCodeType ();
      aDocumentCurrency.setValue (eCurrency.getID ());
      aDocumentCurrency.setName (eCurrency.getDisplayText (aContentLocale));
      aDocumentCurrency.setLanguageID (aContentLocale.getLanguage ());
      ret.setDocumentCurrencyCode (aDocumentCurrency);
    }

    // Buyer customer party
    {
      final CustomerPartyType aCustomerParty = new CustomerPartyType ();
      aCustomerParty.setParty (createSenderParty (aSender, aContentLocale, eCustomization));
      ret.setBuyerCustomerParty (aCustomerParty);
    }

    // Seller supplier party
    {
      final SupplierPartyType aSupplierParty = new SupplierPartyType ();
      aSupplierParty.setParty (createParty (aSupplier, aContentLocale, eCustomization));
      ret.setSellerSupplierParty (aSupplierParty);
    }

    // Payment means
    {
      final PaymentMeansType aPaymentMeans = new PaymentMeansType ();

      final PaymentMeansCodeType aPMCode = new PaymentMeansCodeType ();
      aPMCode.setValue ("31");
      aPaymentMeans.setPaymentMeansCode (aPMCode);

      // Due date = issue date
      final PaymentDueDateType aPMDueDate = new PaymentDueDateType ();
      aPMDueDate.setValue (PDTXMLConverter.getXMLCalendarDate (aOrder.getIssueDate ()));
      aPaymentMeans.setPaymentDueDate (aPMDueDate);

      final PaymentChannelCodeType aPMChannelCode = new PaymentChannelCodeType ();
      aPMChannelCode.setValue ("IBAN");
      aPaymentMeans.setPaymentChannelCode (aPMChannelCode);

      final FinancialAccountType aAccount = new FinancialAccountType ();

      final IDType aAccountID = new IDType ();
      aAccountID.setSchemeID ("IBAN");
      aAccountID.setValue (aSender.getIBAN ());
      aAccount.setID (aAccountID);

      final BranchType aBranch = new BranchType ();
      final FinancialInstitutionType aInstitution = new FinancialInstitutionType ();
      final IDType aInstitutionID = new IDType ();
      aInstitutionID.setSchemeID ("BIC");
      aInstitutionID.setValue (aSender.getBIC ());
      aInstitution.setID (aInstitutionID);
      aBranch.setFinancialInstitution (aInstitution);
      aAccount.setFinancialInstitutionBranch (aBranch);

      aPaymentMeans.setPayeeFinancialAccount (aAccount);

      ret.setPaymentMeans (aPaymentMeans);
    }

    // TaxTotal
    {
      final TaxTotalType aTaxTotal = new TaxTotalType ();
      aTaxTotal.setTaxAmount (createTaxAmount (eUBLCurrency, aOrder.getTotalTaxAmount ().getValue ()));

      // Calculate sub totals
      final Map <TaxKey, TaxValue> aMap = new LinkedHashMap <TaxKey, TaxValue> ();
      for (final ProcOrderItem aOrderItem : aOrder.getOrderItems ())
      {
        // FIXME: tax category
        final TaxKey aKey = new TaxKey (ETaxTypeUN5153.VAT, ETaxCategoryUN5305.S, aOrderItem.getUnitPrice ()
                                                                                            .getVATItem ()
                                                                                            .getPercentage ());
        TaxValue aValue = aMap.get (aKey);
        if (aValue == null)
        {
          aValue = new TaxValue ();
          aMap.put (aKey, aValue);
        }
        aValue.increment (aOrderItem.getTotalNetAmount ().getValue (), aOrderItem.getTotalTaxAmount ().getValue ());
      }

      // Emit subtotals
      for (final Map.Entry <TaxKey, TaxValue> aEntry : aMap.entrySet ())
      {
        final TaxSubtotalType aTaxSubtotal = createTaxSubtotal (eUBLCurrency,
                                                                aEntry.getValue ().getTotalTaxableAmount (),
                                                                aEntry.getValue ().getTotalTaxAmount (),
                                                                aEntry.getKey ().getTaxType (),
                                                                aEntry.getKey ().getTaxCategory (),
                                                                aEntry.getKey ().getPercentage ());
        aTaxTotal.getTaxSubtotal ().add (aTaxSubtotal);
      }

      ret.getTaxTotal ().add (aTaxTotal);
    }

    // AnticipatedMonetaryTotal
    {
      final MonetaryTotalType aMonetaryTotal = new MonetaryTotalType ();

      final LineExtensionAmountType aLineExtension = new LineExtensionAmountType ();
      aLineExtension.setCurrencyID (eUBLCurrency);
      aLineExtension.setValue (aOrder.getTotalNetAmount ().getValue ());
      aMonetaryTotal.setLineExtensionAmount (aLineExtension);

      final TaxExclusiveAmountType aTaxExclusive = new TaxExclusiveAmountType ();
      aTaxExclusive.setCurrencyID (eUBLCurrency);
      aTaxExclusive.setValue (aOrder.getTotalNetAmount ().getValue ());
      aMonetaryTotal.setTaxExclusiveAmount (aTaxExclusive);

      final TaxInclusiveAmountType aTaxInclusive = new TaxInclusiveAmountType ();
      aTaxInclusive.setCurrencyID (eUBLCurrency);
      aTaxInclusive.setValue (aOrder.getTotalGrossAmount ().getValue ());
      aMonetaryTotal.setTaxInclusiveAmount (aTaxInclusive);

      final PayableAmountType aPayable = new PayableAmountType ();
      aPayable.setCurrencyID (eUBLCurrency);
      aPayable.setValue (aOrder.getTotalGrossAmount ().getValue ());
      aMonetaryTotal.setPayableAmount (aPayable);

      ret.setAnticipatedMonetaryTotal (aMonetaryTotal);
    }

    // Order items
    for (final ProcOrderItem aOrderItem : aOrder.getOrderItems ())
    {
      final OrderLineType aLine = new OrderLineType ();

      final LineItemType aLineItem = new LineItemType ();

      // Invoice line ID
      final IDType aLineID = new IDType ();
      aLineID.setValue (Integer.toString (aOrderItem.getID ()));
      aLineItem.setID (aLineID);

      // Invoiced quantity
      final QuantityType aQuantity = new QuantityType ();
      aQuantity.setUnitCode (UnitCodeContentType.fromValue ("C62"));
      aQuantity.setValue (BigDecimal.valueOf (aOrderItem.getQuantity ()));
      aLineItem.setQuantity (aQuantity);

      // Line amount
      final LineExtensionAmountType aLineExtension = new LineExtensionAmountType ();
      aLineExtension.setCurrencyID (eUBLCurrency);
      aLineExtension.setValue (aOrderItem.getTotalNetAmount ().getValue ());
      aLineItem.setLineExtensionAmount (aLineExtension);

      // Item information
      final ItemType aItem = new ItemType ();

      if (StringHelper.hasText (aOrderItem.getDescription ()))
      {
        final DescriptionType aDescription = new DescriptionType ();
        aDescription.setValue (aOrderItem.getDescription ());
        aItem.getDescription ().add (aDescription);
      }

      // Name may be at last 50 chars for PEPPOL
      final String sName = eCustomization.ensureNameLength (aOrderItem.getDescription ());
      if (StringHelper.hasText (sName))
      {
        final NameType aName = new NameType ();
        aName.setValue (sName);
        aItem.setName (aName);
      }

      if (eCustomization.isOrderLineCommodityClassificationAllowed ())
      {
        // Commodity classification
        if (StringHelper.hasText (aOrderItem.getCommodityID ()))
        {
          final CommodityClassificationType aCommodityClassification = new CommodityClassificationType ();
          final CommodityCodeType aCommodityCode = new CommodityCodeType ();
          aCommodityCode.setListAgencyID (aOrderItem.getCommodityScheme ().getID ());
          aCommodityCode.setListAgencyName (aOrderItem.getCommodityScheme ().getDisplayText (aContentLocale));
          aCommodityCode.setValue (aOrderItem.getCommodityID ());
          aCommodityClassification.setCommodityCode (aCommodityCode);
          aItem.getCommodityClassification ().add (aCommodityClassification);
        }
      }

      aLineItem.setItem (aItem);

      // Price
      final PriceType aPrice = new PriceType ();

      final PriceAmountType aPriceAmount = new PriceAmountType ();
      aPriceAmount.setCurrencyID (eUBLCurrency);
      aPriceAmount.setValue (aOrderItem.getUnitPrice ().getNetAmount ().getValue ());
      aPrice.setPriceAmount (aPriceAmount);

      final BaseQuantityType aBaseQuantity = new BaseQuantityType ();
      aBaseQuantity.setUnitCode (UnitCodeContentType.fromValue ("C62"));
      aBaseQuantity.setValue (BigDecimal.ONE);
      aPrice.setBaseQuantity (aBaseQuantity);

      aLineItem.setPrice (aPrice);

      aLine.setLineItem (aLineItem);

      ret.getOrderLine ().add (aLine);
    }

    return ret;
  }
}
