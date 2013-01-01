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
package com.phloc.procurement.party;

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

import com.phloc.commons.CGlobal;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.masterdata.MasterdataUtils;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.CProcLengthConstraints;

@Entity
@Table (name = "paymentterms")
@Access (value = AccessType.PROPERTY)
public final class ProcCustomerPaymentTerm extends AbstractProcObject implements IProcCustomerPaymentTerm
{
  public static final String FIELD_PERCENTAGE = "percentage";
  public static final String FIELD_DESCRIPTION = "description";
  public static final int LENGTH_DESCRIPTION = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_DAYS = "days";

  private ProcCustomer m_aOwner;
  private double m_dPercentage;
  private String m_sDescription;
  private int m_nDays;

  public ProcCustomerPaymentTerm ()
  {}

  public ProcCustomerPaymentTerm (@Nonnull final ProcCustomer aOwner)
  {
    setOwner (aOwner);
  }

  @ManyToOne
  @PrimaryKeyJoinColumn
  @JoinColumn (nullable = false)
  @Nullable
  public ProcCustomer getOwner ()
  {
    return m_aOwner;
  }

  public void setOwner (@Nonnull final ProcCustomer aOwner)
  {
    if (aOwner == null)
      throw new NullPointerException ("owner");
    m_aOwner = aOwner;
  }

  @Column (name = FIELD_PERCENTAGE)
  public double getPercentage ()
  {
    return m_dPercentage;
  }

  @Nonnull
  public EChange setPercentage (final double dPercentage)
  {
    if (EqualsUtils.equals (m_dPercentage, dPercentage))
      return EChange.UNCHANGED;
    m_dPercentage = dPercentage;
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
    if (EqualsUtils.equals (m_sDescription, sRealDescription))
      return EChange.UNCHANGED;
    m_sDescription = sRealDescription;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_DAYS)
  public int getDays ()
  {
    return m_nDays;
  }

  @Nonnull
  public EChange setDays (final int nDays)
  {
    if (m_nDays == nDays)
      return EChange.UNCHANGED;
    m_nDays = nDays;
    return EChange.CHANGED;
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
    final ProcCustomerPaymentTerm rhs = (ProcCustomerPaymentTerm) o;
    return getOwnerID () == rhs.getOwnerID () &&
           EqualsUtils.equals (m_dPercentage, rhs.m_dPercentage) &&
           EqualsUtils.equals (m_sDescription, rhs.m_sDescription) &&
           m_nDays == rhs.m_nDays;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (getOwnerID ())
                            .append (m_dPercentage)
                            .append (m_sDescription)
                            .append (m_nDays)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ownerID", getOwnerID ())
                            .append ("percentage", m_dPercentage)
                            .append ("description", m_sDescription)
                            .append ("days", m_nDays)
                            .toString ();
  }
}
