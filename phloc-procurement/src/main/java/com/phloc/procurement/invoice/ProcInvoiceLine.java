/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.procurement.invoice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.procurement.codelist.ECommodityScheme;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.CProcLengthConstraints;

@Entity
@Table (name = "invoiceline")
@Access (value = AccessType.PROPERTY)
public final class ProcInvoiceLine extends AbstractProcObject implements IProcInvoiceLine
{
  public static final String FIELD_OWNER = "owner";
  public static final String FIELD_ORDERITEM = "orderitem";
  public static final int LENGTH_ORDERITEM = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_LINEID = "lineid";
  public static final int LENGTH_LINEID = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_COMMODITY_SCHEME = "commscheme";
  public static final String FIELD_COMMODITY_ID = "commid";
  public static final int LENGTH_COMMODITY_ID = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_DESCRIPTION = "description";
  public static final int LENGTH_DESCRIPTION = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_QUANTITY = "quantity";
  public static final String FIELD_UNITPRICE = "unitpric";
  public static final String FIELD_TAXCAT = "taxcat";
  public static final String FIELD_REMARK = "remark";
  public static final int LENGTH_REMARK = CProcLengthConstraints.LENGTH_TEXT_LONG;
  private static final Logger s_aLogger = LoggerFactory.getLogger (ProcInvoiceLine.class);

  private ProcInvoice m_aOwner;
  private String m_sOrderItemID;
  private String m_sLineID;
  private ECommodityScheme m_aCommodityScheme;
  private String m_sCommodityID;
  private String m_sDescription;
  private int m_nQuantity;
  private Price m_aUnitPrice;
  private ETaxCategoryUN5305 m_eTaxCategory;
  private String m_sRemark;

  public ProcInvoiceLine ()
  {}

  public ProcInvoiceLine (@Nonnull final ProcInvoice aOwner)
  {
    setOwner (aOwner);
  }

  @ManyToOne
  @PrimaryKeyJoinColumn
  @JoinColumn (name = FIELD_OWNER, nullable = false)
  @Nullable
  public ProcInvoice getOwner ()
  {
    return m_aOwner;
  }

  public void setOwner (@Nonnull final ProcInvoice aOwner)
  {
    if (aOwner == null)
      throw new NullPointerException ("owner");
    m_aOwner = aOwner;
  }

  @Column (name = FIELD_ORDERITEM, length = LENGTH_ORDERITEM)
  @Nullable
  public String getOrderItemID ()
  {
    return m_sOrderItemID;
  }

  @Nonnull
  public EChange setOrderItemID (final String sOrderItemID)
  {
    final String sRealOrderItemID = MasterdataUtils.getEnsuredLength (sOrderItemID, LENGTH_ORDERITEM);
    if (EqualsUtils.equals (sRealOrderItemID, m_sOrderItemID))
      return EChange.UNCHANGED;
    m_sOrderItemID = sRealOrderItemID;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_LINEID, length = LENGTH_LINEID)
  @Nullable
  public String getLineID ()
  {
    return m_sLineID;
  }

  @Nonnull
  public EChange setLineID (@Nullable final String sLineID)
  {
    final String sRealLineID = MasterdataUtils.getEnsuredLength (sLineID, LENGTH_LINEID);
    if (EqualsUtils.equals (sRealLineID, m_sLineID))
      return EChange.UNCHANGED;
    m_sLineID = sRealLineID;
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
    final String sRealCommodityID = MasterdataUtils.getEnsuredLength (sCommodityID, LENGTH_COMMODITY_ID);
    if (EqualsUtils.equals (sRealCommodityID, m_sCommodityID))
      return EChange.UNCHANGED;
    m_sCommodityID = sRealCommodityID;
    return EChange.CHANGED;
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
    if (EqualsUtils.equals (sRealDescription, m_sDescription))
      return EChange.UNCHANGED;
    m_sDescription = sRealDescription;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_QUANTITY)
  public int getQuantity ()
  {
    return m_nQuantity;
  }

  @Nonnull
  public EChange setQuantity (final int nQuantity)
  {
    if (nQuantity == m_nQuantity)
      return EChange.UNCHANGED;
    m_nQuantity = nQuantity;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_UNITPRICE)
  @Nullable
  public Price getUnitPrice ()
  {
    return m_aUnitPrice;
  }

  @DevelopersNote ("Used by EclipseLink only")
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

  @Column (name = FIELD_TAXCAT)
  @Nullable
  public ETaxCategoryUN5305 getTaxCategory ()
  {
    return m_eTaxCategory;
  }

  @Transient
  @Nullable
  public String getTaxCategoryID ()
  {
    return m_eTaxCategory == null ? null : m_eTaxCategory.getID ();
  }

  @Nonnull
  public EChange setTaxCategory (@Nullable final ETaxCategoryUN5305 eTaxCategory)
  {
    if (EqualsUtils.equals (eTaxCategory, m_eTaxCategory))
      return EChange.UNCHANGED;
    m_eTaxCategory = eTaxCategory;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_REMARK, length = LENGTH_REMARK)
  @Nullable
  public String getRemark ()
  {
    return m_sRemark;
  }

  @Nonnull
  public EChange setRemark (@Nullable final String sRemark)
  {
    final String sRealRemark = MasterdataUtils.getEnsuredLength (sRemark, LENGTH_REMARK);
    if (EqualsUtils.equals (sRealRemark, m_sRemark))
      return EChange.UNCHANGED;
    m_sRemark = sRealRemark;
    return EChange.CHANGED;
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalNetAmount ()
  {
    if (m_aUnitPrice == null || m_nQuantity < 0)
    {
      s_aLogger.warn ("Unit price = " + m_aUnitPrice + "; quantity = " + m_nQuantity + " => no getTotalNetAmount");
      return null;
    }
    return m_aUnitPrice.getNetAmount ().getMultiplied (m_nQuantity);
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalGrossAmount ()
  {
    if (m_aUnitPrice == null || m_nQuantity < 0)
    {
      s_aLogger.warn ("Unit price = " + m_aUnitPrice + "; quantity = " + m_nQuantity + " => no getTotalGrossAmount");
      return null;
    }
    return m_aUnitPrice.getGrossAmount ().getMultiplied (m_nQuantity);
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalTaxAmount ()
  {
    if (m_aUnitPrice == null || m_nQuantity < 0)
    {
      s_aLogger.warn ("Unit price = " + m_aUnitPrice + "; quantity = " + m_nQuantity + " => no getTotalTaxAmount");
      return null;
    }
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
    final ProcInvoiceLine rhs = (ProcInvoiceLine) o;
    return getOwnerID () == rhs.getOwnerID () &&
           EqualsUtils.equals (m_sOrderItemID, rhs.m_sOrderItemID) &&
           EqualsUtils.equals (m_sLineID, rhs.m_sLineID) &&
           EqualsUtils.equals (m_aCommodityScheme, rhs.m_aCommodityScheme) &&
           EqualsUtils.equals (m_sCommodityID, rhs.m_sCommodityID) &&
           EqualsUtils.equals (m_sDescription, rhs.m_sDescription) &&
           m_nQuantity == rhs.m_nQuantity &&
           EqualsUtils.equals (m_aUnitPrice, rhs.m_aUnitPrice) &&
           EqualsUtils.equals (m_eTaxCategory, rhs.m_eTaxCategory) &&
           EqualsUtils.equals (m_sRemark, rhs.m_sRemark);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (getOwnerID ())
                            .append (m_sOrderItemID)
                            .append (m_sLineID)
                            .append (m_aCommodityScheme)
                            .append (m_sCommodityID)
                            .append (m_sDescription)
                            .append (m_nQuantity)
                            .append (m_aUnitPrice)
                            .append (m_eTaxCategory)
                            .append (m_sRemark)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ownerID", getOwnerID ())
                            .append ("orderItemID", m_sOrderItemID)
                            .append ("lineID", m_sLineID)
                            .append ("commodityScheme", m_aCommodityScheme)
                            .append ("commodityID", m_sCommodityID)
                            .append ("description", m_sDescription)
                            .append ("quantity", m_nQuantity)
                            .append ("unitPrice", m_aUnitPrice)
                            .append ("taxCategory", m_eTaxCategory)
                            .append ("remark", m_sRemark)
                            .toString ();
  }
}
