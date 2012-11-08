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
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContactType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyTaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.BuildingNumberType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CityNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CompanyIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ElectronicMailType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EndpointIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PostalZoneType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PostboxType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StreetNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TelephoneType;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.masterdata.tax.ETaxTypeUN5153;
import com.phloc.procurement.party.AbstractProcParty;
import com.phloc.procurement.party.ProcMasterData;

@Immutable
abstract class AbstractUBL20Serializer
{
  @MustImplementEqualsAndHashcode
  protected static final class TaxKey
  {
    private final ETaxTypeUN5153 m_eTaxType;
    private final ETaxCategoryUN5305 m_eTaxCategory;
    private final BigDecimal m_aPercentage;

    public TaxKey (@Nonnull final ETaxTypeUN5153 eTaxType,
                   @Nonnull final ETaxCategoryUN5305 eTaxCategory,
                   @Nonnull final BigDecimal aPercentage)
    {
      if (eTaxType == null)
        throw new NullPointerException ("taxType");
      if (eTaxCategory == null)
        throw new NullPointerException ("taxCategory");
      if (aPercentage == null)
        throw new NullPointerException ("percentage");
      m_eTaxType = eTaxType;
      m_eTaxCategory = eTaxCategory;
      m_aPercentage = aPercentage;
    }

    @Nonnull
    public ETaxTypeUN5153 getTaxType ()
    {
      return m_eTaxType;
    }

    @Nonnull
    public ETaxCategoryUN5305 getTaxCategory ()
    {
      return m_eTaxCategory;
    }

    @Nonnull
    public BigDecimal getPercentage ()
    {
      return m_aPercentage;
    }

    @Override
    public boolean equals (final Object o)
    {
      if (o == this)
        return true;
      if (!(o instanceof TaxKey))
        return false;
      final TaxKey rhs = (TaxKey) o;
      return m_eTaxType.equals (rhs.m_eTaxType) &&
             m_eTaxCategory.equals (rhs.m_eTaxCategory) &&
             EqualsUtils.equals (m_aPercentage, rhs.m_aPercentage);
    }

    @Override
    public int hashCode ()
    {
      return new HashCodeGenerator (this).append (m_eTaxType)
                                         .append (m_eTaxCategory)
                                         .append (m_aPercentage)
                                         .getHashCode ();
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("typ", m_eTaxType)
                                         .append ("category", m_eTaxCategory)
                                         .append ("percentage", m_aPercentage)
                                         .toString ();
    }
  }

  protected static final class TaxValue
  {
    private BigDecimal m_aTaxableAmount = BigDecimal.ZERO;
    private BigDecimal m_aTaxAmount = BigDecimal.ZERO;

    public void increment (@Nonnull final BigDecimal aTaxableAmount, @Nonnull final BigDecimal aTaxAmount)
    {
      m_aTaxableAmount = m_aTaxableAmount.add (aTaxableAmount);
      m_aTaxAmount = m_aTaxAmount.add (aTaxAmount);
    }

    @Nonnull
    public BigDecimal getTotalTaxableAmount ()
    {
      return m_aTaxableAmount;
    }

    @Nonnull
    public BigDecimal getTotalTaxAmount ()
    {
      return m_aTaxAmount;
    }
  }

  protected AbstractUBL20Serializer ()
  {}

  @Nullable
  protected static CountryType createCountry (@Nullable final Locale aCountry,
                                              @Nonnull final Locale aContentLocale,
                                              @Nonnull final IUBLCACCustomization aCustomization)
  {
    if (aCountry == null)
      return null;

    final CountryType ret = new CountryType ();
    final IdentificationCodeType aCountryID = new IdentificationCodeType ();
    aCountryID.setValue (aCountry.getCountry ());
    ret.setIdentificationCode (aCountryID);
    if (aCustomization.isCountryNameAllowed ())
    {
      final NameType aCountryName = new NameType ();
      aCountryName.setLanguageID (aContentLocale.getLanguage ());
      aCountryName.setValue (aCountry.getDisplayCountry (aContentLocale));
      ret.setName (aCountryName);
    }
    return ret;
  }

  @Nonnull
  protected static PartyType createParty (@Nonnull final AbstractProcParty aParty,
                                          @Nonnull final Locale aContentLocale,
                                          @Nonnull final IUBLCACCustomization aCustomization)
  {
    final PartyType ret = new PartyType ();

    // Party ID
    if (StringHelper.hasText (aParty.getEntityID ()))
    {
      final PartyIdentificationType aPartyID = new PartyIdentificationType ();
      final IDType aID = new IDType ();
      aID.setSchemeID (aParty.getEntityIDScheme ().getID ());
      aID.setValue (aParty.getEntityID ());
      aPartyID.setID (aID);
      ret.getPartyIdentification ().add (aPartyID);
    }

    // Party Name
    if (StringHelper.hasText (aParty.getName ()))
    {
      final PartyNameType aPartyName = new PartyNameType ();
      final NameType aName = new NameType ();
      aName.setValue (aParty.getName ());
      aPartyName.setName (aName);
      ret.getPartyName ().add (aPartyName);
    }

    // Endpoint ID
    if (StringHelper.hasText (aParty.getEndpointID ()))
    {
      final EndpointIDType aEndpointID = new EndpointIDType ();
      aEndpointID.setValue (aParty.getEndpointID ());
      ret.setEndpointID (aEndpointID);
    }

    // Postal address
    final AddressType aAddress = new AddressType ();

    if (StringHelper.hasText (aParty.getStreet ()))
    {
      final StreetNameType aStreetName = new StreetNameType ();
      aStreetName.setValue (aParty.getStreet ());
      aAddress.setStreetName (aStreetName);
    }

    if (StringHelper.hasText (aParty.getBuildingNumber ()))
    {
      final BuildingNumberType aBuildingNumber = new BuildingNumberType ();
      aBuildingNumber.setValue (aParty.getBuildingNumber ());
      aAddress.setBuildingNumber (aBuildingNumber);
    }

    if (StringHelper.hasText (aParty.getPostbox ()))
    {
      final PostboxType aPostbox = new PostboxType ();
      aPostbox.setValue (aParty.getPostbox ());
      aAddress.setPostbox (aPostbox);
    }

    if (StringHelper.hasText (aParty.getZipCode ()))
    {
      final PostalZoneType aPostalZone = new PostalZoneType ();
      aPostalZone.setValue (aParty.getZipCode ());
      aAddress.setPostalZone (aPostalZone);
    }

    if (StringHelper.hasText (aParty.getCity ()))
    {
      final CityNameType aCityName = new CityNameType ();
      aCityName.setValue (aParty.getCity ());
      aAddress.setCityName (aCityName);
    }

    aAddress.setCountry (createCountry (aParty.getCountry (), aContentLocale, aCustomization));
    ret.setPostalAddress (aAddress);

    // Tax scheme
    final PartyTaxSchemeType aPartyTaxScheme = new PartyTaxSchemeType ();
    final CompanyIDType aCompanyID = new CompanyIDType ();
    aCompanyID.setValue (aParty.getVATIN ());
    aPartyTaxScheme.setCompanyID (aCompanyID);
    aPartyTaxScheme.setTaxScheme (createTaxScheme (ETaxTypeUN5153.VAT));
    ret.getPartyTaxScheme ().add (aPartyTaxScheme);

    return ret;
  }

  @Nonnull
  protected static PartyType createSenderParty (@Nonnull final ProcMasterData aSender,
                                                @Nonnull final Locale aContentLocale,
                                                @Nonnull final IUBLCACCustomization aCustomization)
  {
    final PartyType ret = createParty (aSender, aContentLocale, aCustomization);

    // Create contact person
    final ContactType aContact = new ContactType ();

    if (StringHelper.hasText (aSender.getEmail ()))
    {
      final ElectronicMailType aEmail = new ElectronicMailType ();
      aEmail.setValue (aSender.getEmail ());
      aContact.setElectronicMail (aEmail);
    }

    if (StringHelper.hasText (aSender.getTelephone ()))
    {
      final TelephoneType aTelephone = new TelephoneType ();
      aTelephone.setValue (aSender.getTelephone ());
      aContact.setTelephone (aTelephone);
    }

    if (aCustomization.isPartyContactNameAllowed ())
    {
      final String sContactName = StringHelper.getConcatenatedOnDemand (aSender.getFirstName (),
                                                                        " ",
                                                                        aSender.getLastName ());
      if (StringHelper.hasText (sContactName))
      {
        final NameType aName = new NameType ();
        aName.setValue (sContactName);
        aContact.setName (aName);
      }
    }

    ret.setContact (aContact);

    return ret;
  }

  @Nonnull
  protected static TaxAmountType createTaxAmount (final CurrencyCodeContentType eUBLCurrency,
                                                  final BigDecimal aTaxAmountValue)
  {
    final TaxAmountType aTaxAmount = new TaxAmountType ();
    aTaxAmount.setCurrencyID (eUBLCurrency);
    aTaxAmount.setValue (aTaxAmountValue);
    return aTaxAmount;
  }

  protected static TaxSchemeType createTaxScheme (final ETaxTypeUN5153 eTaxType)
  {
    final TaxSchemeType aTaxScheme = new TaxSchemeType ();
    final IDType aTaxSchemeID = new IDType ();
    aTaxSchemeID.setSchemeAgencyID ("6");
    aTaxSchemeID.setSchemeID ("UN/ECE 5153");
    aTaxSchemeID.setValue (eTaxType.getID ());
    aTaxScheme.setID (aTaxSchemeID);
    return aTaxScheme;
  }

  @Nonnull
  protected static TaxSubtotalType createTaxSubtotal (final CurrencyCodeContentType eUBLCurrency,
                                                      final BigDecimal aTaxbableAmountValue,
                                                      final BigDecimal aTaxAmountValue,
                                                      final ETaxTypeUN5153 eTaxType,
                                                      final ETaxCategoryUN5305 eTaxCategory,
                                                      final BigDecimal aPercentageValue)
  {
    final TaxSubtotalType aTaxSubtotal = new TaxSubtotalType ();

    final TaxableAmountType aTaxableAmount = new TaxableAmountType ();
    aTaxableAmount.setCurrencyID (eUBLCurrency);
    aTaxableAmount.setValue (aTaxbableAmountValue);
    aTaxSubtotal.setTaxableAmount (aTaxableAmount);

    aTaxSubtotal.setTaxAmount (createTaxAmount (eUBLCurrency, aTaxAmountValue));

    // Tax category
    {
      final TaxCategoryType aTaxCategory = new TaxCategoryType ();

      final IDType aTaxCategoryID = new IDType ();
      aTaxCategoryID.setSchemeAgencyID ("6");
      aTaxCategoryID.setSchemeID ("UN/ECE 5305");
      aTaxCategoryID.setValue (eTaxCategory.getID ());
      aTaxCategory.setID (aTaxCategoryID);

      final PercentType aPercent = new PercentType ();
      aPercent.setValue (aPercentageValue);
      aTaxCategory.setPercent (aPercent);

      aTaxCategory.setTaxScheme (createTaxScheme (eTaxType));
      aTaxSubtotal.setTaxCategory (aTaxCategory);
    }

    return aTaxSubtotal;
  }
}
