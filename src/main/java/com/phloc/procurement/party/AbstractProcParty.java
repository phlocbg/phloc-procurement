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
package com.phloc.procurement.party;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.db.jpa.converter.JPALocaleConverter;
import com.phloc.procurement.domain.AbstractProcDeletableObject;
import com.phloc.procurement.domain.CProcLengthConstraints;
import com.phloc.procurement.idscheme.EntityIDScheme;
import com.phloc.procurement.idscheme.JPAEntityIDSchemeConverter;

/**
 * Base class for customer, supplier and master data domain objects.
 * 
 * @author philip
 */
@MappedSuperclass
@Access (value = AccessType.PROPERTY)
@Converters ({ @Converter (name = "locale", converterClass = JPALocaleConverter.class),
              @Converter (name = "entityidscheme", converterClass = JPAEntityIDSchemeConverter.class) })
public abstract class AbstractProcParty extends AbstractProcDeletableObject implements IProcParty
{
  public static final String FIELD_IDSCHEME = "idscheme";
  public static final int LENGTH_IDSCHEME = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_IDVALUE = "idvalue";
  public static final int LENGTH_IDVALUE = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_ENDPOINTID = "endpointid";
  public static final int LENGTH_ENDPOINTID = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_NAME = "name";
  public static final int LENGTH_NAME = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_STREET = "street";
  public static final int LENGTH_STREET = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_BUILDINGNUMBER = "buildingno";
  public static final int LENGTH_BUILDINGNUMBER = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_POSTBOX = "postbox";
  public static final int LENGTH_POSTBOX = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_ZIPCODE = "zipcode";
  public static final int LENGTH_ZIPCODE = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_CITY = "city";
  public static final int LENGTH_CITY = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_COUNTRY = "country";
  public static final int LENGTH_COUNTRY = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_REGISTRATIONNAME = "regname";
  public static final int LENGTH_REGISTRATIONNAME = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_REGISTRATIONNUMBER = "regnumber";
  public static final int LENGTH_REGISTRATIONNUMBER = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_VATIN = "vatid";
  public static final int LENGTH_VATIN = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_BIC = "bic";
  public static final int LENGTH_BIC = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_IBAN = "iban";
  public static final int LENGTH_IBAN = CProcLengthConstraints.LENGTH_TEXT;

  private EntityIDScheme m_eEntityIDScheme;
  private String m_sEntityID;
  private String m_sEndpointID;
  private String m_sName;
  private String m_sStreet;
  private String m_sBuildingNumber;
  private String m_sPostbox;
  private String m_sZipCode;
  private String m_sCity;
  private Locale m_aCountry;
  private String m_sRegistrationName;
  private String m_sCompanyRegistrationNumber;
  private String m_sVATIN;
  private String m_sBIC;
  private String m_sIBAN;

  @Column (name = FIELD_IDSCHEME, length = LENGTH_IDSCHEME)
  @Convert ("entityidscheme")
  @Nullable
  public final EntityIDScheme getEntityIDScheme ()
  {
    return m_eEntityIDScheme;
  }

  @Nonnull
  public final EChange setEntityIDScheme (@Nonnull final EntityIDScheme eEntityIDScheme)
  {
    if (EqualsUtils.equals (m_eEntityIDScheme, eEntityIDScheme))
      return EChange.UNCHANGED;
    m_eEntityIDScheme = eEntityIDScheme;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_IDVALUE, length = LENGTH_IDVALUE)
  @Nullable
  public final String getEntityID ()
  {
    return m_sEntityID;
  }

  @Nonnull
  public final EChange setEntityID (@Nonnull final String sEntityID)
  {
    if (EqualsUtils.equals (m_sEntityID, sEntityID))
      return EChange.UNCHANGED;
    m_sEntityID = sEntityID;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_ENDPOINTID, length = LENGTH_ENDPOINTID)
  @Nullable
  public final String getEndpointID ()
  {
    return m_sEndpointID;
  }

  @Nonnull
  public final EChange setEndpointID (@Nullable final String sEndpointID)
  {
    if (EqualsUtils.equals (m_sEndpointID, sEndpointID))
      return EChange.UNCHANGED;
    m_sEndpointID = sEndpointID;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_NAME, length = LENGTH_NAME)
  @Nullable
  public final String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public final EChange setName (@Nonnull final String sName)
  {
    if (EqualsUtils.equals (m_sName, sName))
      return EChange.UNCHANGED;
    m_sName = sName;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_STREET, length = LENGTH_STREET)
  @Nullable
  public final String getStreet ()
  {
    return m_sStreet;
  }

  @Nonnull
  public final EChange setStreet (@Nonnull final String sStreet)
  {
    if (EqualsUtils.equals (m_sStreet, sStreet))
      return EChange.UNCHANGED;
    m_sStreet = sStreet;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_BUILDINGNUMBER, length = LENGTH_BUILDINGNUMBER)
  @Nullable
  public final String getBuildingNumber ()
  {
    return m_sBuildingNumber;
  }

  @Nonnull
  public final EChange setBuildingNumber (@Nonnull final String sBuildingNumber)
  {
    if (EqualsUtils.equals (m_sBuildingNumber, sBuildingNumber))
      return EChange.UNCHANGED;
    m_sBuildingNumber = sBuildingNumber;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_POSTBOX, length = LENGTH_POSTBOX)
  @Nullable
  public final String getPostbox ()
  {
    return m_sPostbox;
  }

  @Nonnull
  public final EChange setPostbox (@Nonnull final String sPostbox)
  {
    if (EqualsUtils.equals (m_sPostbox, sPostbox))
      return EChange.UNCHANGED;
    m_sPostbox = sPostbox;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_ZIPCODE, length = LENGTH_ZIPCODE)
  @Nullable
  public final String getZipCode ()
  {
    return m_sZipCode;
  }

  @Nonnull
  public final EChange setZipCode (@Nonnull final String sZipCode)
  {
    if (EqualsUtils.equals (m_sZipCode, sZipCode))
      return EChange.UNCHANGED;
    m_sZipCode = sZipCode;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_CITY, length = LENGTH_CITY)
  @Nullable
  public final String getCity ()
  {
    return m_sCity;
  }

  @Nonnull
  public final EChange setCity (@Nonnull final String sCity)
  {
    if (EqualsUtils.equals (m_sCity, sCity))
      return EChange.UNCHANGED;
    m_sCity = sCity;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_COUNTRY, length = LENGTH_COUNTRY)
  @Convert ("locale")
  @Basic
  @Nullable
  public final Locale getCountry ()
  {
    return m_aCountry;
  }

  @Nonnull
  public final EChange setCountry (@Nonnull final Locale aCountry)
  {
    if (EqualsUtils.equals (m_aCountry, aCountry))
      return EChange.UNCHANGED;
    m_aCountry = aCountry;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_REGISTRATIONNAME, length = LENGTH_REGISTRATIONNAME)
  @Nullable
  public final String getRegistrationName ()
  {
    return m_sRegistrationName;
  }

  @Nonnull
  public final EChange setRegistrationName (@Nonnull final String sRegistrationName)
  {
    if (EqualsUtils.equals (m_sRegistrationName, sRegistrationName))
      return EChange.UNCHANGED;
    m_sRegistrationName = sRegistrationName;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_REGISTRATIONNUMBER, length = LENGTH_REGISTRATIONNUMBER)
  @Nullable
  public final String getCompanyRegistrationNumber ()
  {
    return m_sCompanyRegistrationNumber;
  }

  @Nonnull
  public final EChange setCompanyRegistrationNumber (@Nonnull final String sCompanyRegistrationNumber)
  {
    if (EqualsUtils.equals (m_sCompanyRegistrationNumber, sCompanyRegistrationNumber))
      return EChange.UNCHANGED;
    m_sCompanyRegistrationNumber = sCompanyRegistrationNumber;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_VATIN, length = LENGTH_VATIN)
  @Nullable
  public final String getVATIN ()
  {
    return m_sVATIN;
  }

  @Nonnull
  public final EChange setVATIN (@Nonnull final String sVATID)
  {
    if (EqualsUtils.equals (m_sVATIN, sVATID))
      return EChange.UNCHANGED;
    m_sVATIN = sVATID;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_BIC, length = LENGTH_BIC)
  @Nullable
  public final String getBIC ()
  {
    return m_sBIC;
  }

  @Nonnull
  public final EChange setBIC (@Nonnull final String sBIC)
  {
    if (EqualsUtils.equals (m_sBIC, sBIC))
      return EChange.UNCHANGED;
    m_sBIC = sBIC;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_IBAN, length = LENGTH_IBAN)
  @Nullable
  public final String getIBAN ()
  {
    return m_sIBAN;
  }

  @Nonnull
  public final EChange setIBAN (@Nonnull final String sIBAN)
  {
    if (EqualsUtils.equals (m_sIBAN, sIBAN))
      return EChange.UNCHANGED;
    m_sIBAN = sIBAN;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AbstractProcParty rhs = (AbstractProcParty) o;
    return EqualsUtils.equals (m_eEntityIDScheme, rhs.m_eEntityIDScheme) &&
           EqualsUtils.equals (m_sEntityID, rhs.m_sEntityID) &&
           EqualsUtils.equals (m_sEndpointID, rhs.m_sEndpointID) &&
           EqualsUtils.equals (m_sName, rhs.m_sName) &&
           EqualsUtils.equals (m_sStreet, rhs.m_sStreet) &&
           EqualsUtils.equals (m_sBuildingNumber, rhs.m_sBuildingNumber) &&
           EqualsUtils.equals (m_sPostbox, rhs.m_sPostbox) &&
           EqualsUtils.equals (m_sZipCode, rhs.m_sZipCode) &&
           EqualsUtils.equals (m_sCity, rhs.m_sCity) &&
           EqualsUtils.equals (m_aCountry, rhs.m_aCountry) &&
           EqualsUtils.equals (m_sRegistrationName, rhs.m_sRegistrationName) &&
           EqualsUtils.equals (m_sCompanyRegistrationNumber, rhs.m_sCompanyRegistrationNumber) &&
           EqualsUtils.equals (m_sVATIN, rhs.m_sVATIN) &&
           EqualsUtils.equals (m_sBIC, rhs.m_sBIC) &&
           EqualsUtils.equals (m_sIBAN, rhs.m_sIBAN);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_eEntityIDScheme)
                            .append (m_sEntityID)
                            .append (m_sEndpointID)
                            .append (m_sName)
                            .append (m_sStreet)
                            .append (m_sBuildingNumber)
                            .append (m_sPostbox)
                            .append (m_sZipCode)
                            .append (m_sCity)
                            .append (m_aCountry)
                            .append (m_sRegistrationName)
                            .append (m_sCompanyRegistrationNumber)
                            .append (m_sVATIN)
                            .append (m_sBIC)
                            .append (m_sIBAN)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("entityIDScheme", m_eEntityIDScheme)
                            .append ("entityID", m_sEntityID)
                            .append ("endpointID", m_sEndpointID)
                            .append ("name", m_sName)
                            .append ("street", m_sStreet)
                            .append ("buildingNumber", m_sBuildingNumber)
                            .append ("postbox", m_sPostbox)
                            .append ("zipCode", m_sZipCode)
                            .append ("city", m_sCity)
                            .append ("country", m_aCountry)
                            .append ("registrationName", m_sRegistrationName)
                            .append ("companyRegistrationNumber", m_sCompanyRegistrationNumber)
                            .append ("VATIN", m_sVATIN)
                            .append ("BIC", m_sBIC)
                            .append ("IBAN", m_sIBAN)
                            .toString ();
  }
}
