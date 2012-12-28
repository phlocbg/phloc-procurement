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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.masterdata.MasterdataUtils;
import com.phloc.masterdata.currency.IReadonlyCurrencyValue;
import com.phloc.masterdata.price.IReadonlyPrice;
import com.phloc.masterdata.price.Price;
import com.phloc.procurement.codelist.ECommodityScheme;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.CProcLengthConstraints;

@Entity
@Table (name = "orderitem")
@Access (value = AccessType.PROPERTY)
public final class ProcOrderItem extends AbstractProcObject implements IProcOrderItem
{
  public static final String FIELD_OWNER = "owner";
  public static final String FIELD_DESCRIPTION = "description";
  public static final int LENGTH_DESCRIPTION = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_NOTE = "note";
  public static final int LENGTH_NOTE = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_QUANTITY = "quantity";
  public static final String FIELD_COMMODITY_SCHEME = "commscheme";
  public static final String FIELD_COMMODITY_ID = "commid";
  public static final int LENGTH_COMMODITY_ID = CProcLengthConstraints.LENGTH_TEXT_LONG;

  private ProcOrder m_aOwner;
  private String m_sDescription;
  private String m_sNote;
  private int m_nQuantity;
  private Price m_aUnitPrice;
  private ECommodityScheme m_aCommodityScheme;
  private String m_sCommodityID;

  public ProcOrderItem ()
  {}

  public ProcOrderItem (@Nonnull final ProcOrder aOwner)
  {
    setOwner (aOwner);
  }

  @ManyToOne
  @PrimaryKeyJoinColumn
  @JoinColumn (name = FIELD_OWNER, nullable = false)
  @Nullable
  public ProcOrder getOwner ()
  {
    return m_aOwner;
  }

  public void setOwner (@Nonnull final ProcOrder aOwner)
  {
    if (aOwner == null)
      throw new NullPointerException ("owner");
    m_aOwner = aOwner;
  }

  @Column (name = FIELD_DESCRIPTION, length = LENGTH_DESCRIPTION)
  @Nullable
  public String getDescription ()
  {
    return m_sDescription;
  }

  @Nonnull
  public EChange setDescription (@Nullable final String sDescription)
  {
    final String sRealDescription = MasterdataUtils.getEnsuredLength (sDescription, LENGTH_DESCRIPTION);
    if (EqualsUtils.equals (m_sDescription, sRealDescription))
      return EChange.UNCHANGED;
    m_sDescription = sRealDescription;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_NOTE, length = LENGTH_NOTE)
  @Nullable
  public String getNote ()
  {
    return m_sNote;
  }

  @Nonnull
  public EChange setNote (@Nullable final String sNote)
  {
    final String sRealNote = MasterdataUtils.getEnsuredLength (sNote, LENGTH_NOTE);
    if (EqualsUtils.equals (m_sNote, sRealNote))
      return EChange.UNCHANGED;
    m_sNote = sRealNote;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_QUANTITY)
  public int getQuantity ()
  {
    return m_nQuantity;
  }

  @Nonnull
  public EChange setQuantity (@Nonnull final int nQuantity)
  {
    if (m_nQuantity == nQuantity)
      return EChange.UNCHANGED;
    m_nQuantity = nQuantity;
    return EChange.CHANGED;
  }

  @Embedded
  @Nullable
  public Price getUnitPrice ()
  {
    return m_aUnitPrice;
  }

  @DevelopersNote ("For EclipseLink only")
  @Nonnull
  @Deprecated
  public EChange setUnitPrice (@Nullable final Price aUnitPrice)
  {
    if (EqualsUtils.equals (aUnitPrice, m_aUnitPrice))
      return EChange.UNCHANGED;
    m_aUnitPrice = aUnitPrice;
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setUnitPrice (@Nonnull final IReadonlyPrice aUnitPrice)
  {
    if (aUnitPrice == null)
      throw new NullPointerException ("unitPrice");

    m_aUnitPrice = new Price (aUnitPrice);
    return EChange.CHANGED;
  }

  @Column (name = FIELD_COMMODITY_SCHEME)
  @Nullable
  public ECommodityScheme getCommodityScheme ()
  {
    return m_aCommodityScheme;
  }

  @Transient
  @Nullable
  public String getCommoditySchemeID ()
  {
    return m_aCommodityScheme == null ? null : m_aCommodityScheme.getID ();
  }

  @Nonnull
  public EChange setCommodityScheme (@Nullable final ECommodityScheme aCommodityScheme)
  {
    if (EqualsUtils.equals (aCommodityScheme, m_aCommodityScheme))
      return EChange.UNCHANGED;
    m_aCommodityScheme = aCommodityScheme;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_COMMODITY_ID, length = LENGTH_COMMODITY_ID)
  @Nullable
  public String getCommodityID ()
  {
    return m_sCommodityID;
  }

  @Nonnull
  public EChange setCommodityID (@Nullable final String sCommodityID)
  {
    final String sRealCommodityID = MasterdataUtils.getEnsuredLength (sCommodityID, LENGTH_NOTE);
    if (EqualsUtils.equals (sRealCommodityID, m_sCommodityID))
      return EChange.UNCHANGED;
    m_sCommodityID = sRealCommodityID;
    return EChange.CHANGED;
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalNetAmount ()
  {
    if (m_aUnitPrice == null || m_nQuantity < 0)
      return null;
    return m_aUnitPrice.getNetAmount ().getMultiplied (m_nQuantity);
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalGrossAmount ()
  {
    if (m_aUnitPrice == null || m_nQuantity < 0)
      return null;
    return m_aUnitPrice.getGrossAmount ().getMultiplied (m_nQuantity);
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalTaxAmount ()
  {
    if (m_aUnitPrice == null || m_nQuantity < 0)
      return null;
    return m_aUnitPrice.getTaxAmount ().getMultiplied (m_nQuantity);
  }

  @Transient
  public int getOwnerID ()
  {
    return m_aOwner == null ? CGlobal.ILLEGAL_UINT : m_aOwner.getID ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcOrderItem rhs = (ProcOrderItem) o;
    return getOwnerID () == rhs.getOwnerID () &&
           EqualsUtils.equals (m_sDescription, rhs.m_sDescription) &&
           EqualsUtils.equals (m_sNote, rhs.m_sNote) &&
           m_nQuantity == rhs.m_nQuantity &&
           EqualsUtils.equals (m_aUnitPrice, rhs.m_aUnitPrice) &&
           EqualsUtils.equals (m_aCommodityScheme, rhs.m_aCommodityScheme) &&
           EqualsUtils.equals (m_sCommodityID, rhs.m_sCommodityID);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (getOwnerID ())
                            .append (m_sDescription)
                            .append (m_sNote)
                            .append (m_nQuantity)
                            .append (m_aUnitPrice)
                            .append (m_aCommodityScheme)
                            .append (m_sCommodityID)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ownerID", getOwnerID ())
                            .append ("description", m_sDescription)
                            .append ("note", m_sNote)
                            .append ("quantity", m_nQuantity)
                            .append ("unitPrice", m_aUnitPrice)
                            .append ("commodityScheme", m_aCommodityScheme)
                            .append ("commodityID", m_sCommodityID)
                            .toString ();
  }
}
