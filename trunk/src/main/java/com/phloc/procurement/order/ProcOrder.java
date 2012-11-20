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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Index;
import org.joda.time.LocalDate;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.db.jpa.converter.JPAJodaLocalDateConverter;
import com.phloc.masterdata.MasterdataUtils;
import com.phloc.masterdata.currency.CurrencyValue;
import com.phloc.masterdata.currency.ICurrencyValue;
import com.phloc.masterdata.currency.IReadonlyCurrencyValue;
import com.phloc.masterdata.trade.EIncoterm;
import com.phloc.procurement.domain.AbstractProcDeletableObject;
import com.phloc.procurement.domain.CProcLengthConstraints;

/**
 * Domain object for an order.
 * 
 * @author philip
 */
@Entity
@Table (name = "orders")
@Access (value = AccessType.PROPERTY)
@Converter (name = "joda-localdate", converterClass = JPAJodaLocalDateConverter.class)
public final class ProcOrder extends AbstractProcDeletableObject implements IProcOrder
{
  public static final String FIELD_ORDER_NUMBER = "ordernumber";
  public static final int LENGTH_ORDER_NUMBER = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_ORDER_ITEMS = "orderitems";
  public static final String FIELD_ISSUE_DATE = "issuedate";
  public static final String FIELD_DELIVERY_START_DATE = "delstartdate";
  public static final String FIELD_DELIVERY_END_DATE = "delenddate";
  public static final String FIELD_INCOTERM = "incoterm";
  public static final String FIELD_ATTACHMENTS = "attachments";

  /** The order number */
  private String m_sOrderNumber;

  /** The order details */
  private List <ProcOrderItem> m_aOrderItems;

  /** The date when the order was issued */
  private LocalDate m_aIssueDate;

  /** The delivery start date */
  private LocalDate m_aDeliveryStartDate;

  /** The delivery end date */
  private LocalDate m_aDeliveryEndDate;

  /** The used delivery terms */
  private EIncoterm m_eIncoterm;

  /** The optional attachments */
  private List <ProcOrderAttachment> m_aAttachments;

  public ProcOrder ()
  {}

  @Column (name = FIELD_ORDER_NUMBER, length = LENGTH_ORDER_NUMBER)
  @Index
  @Nullable
  public String getOrderNumber ()
  {
    return m_sOrderNumber;
  }

  @Nonnull
  public EChange setOrderNumber (@Nullable final String sOrderNumber)
  {
    final String sRealOrderNumber = MasterdataUtils.getEnsuredLength (sOrderNumber, LENGTH_ORDER_NUMBER);
    if (EqualsUtils.equals (sRealOrderNumber, m_sOrderNumber))
      return EChange.UNCHANGED;
    m_sOrderNumber = sRealOrderNumber;
    return EChange.CHANGED;
  }

  @OneToMany (mappedBy = ProcOrderItem.FIELD_OWNER, cascade = CascadeType.ALL)
  @JoinColumn (name = FIELD_ORDER_ITEMS)
  @ReturnsMutableObject (reason = "EclipseLink managed object")
  @Nullable
  public List <ProcOrderItem> getOrderItems ()
  {
    return m_aOrderItems;
  }

  @Nonnull
  public EChange setOrderItems (@Nullable final List <ProcOrderItem> aOrderItems)
  {
    final List <ProcOrderItem> aRealOrderItems = ContainerHelper.isEmpty (aOrderItems) ? null : aOrderItems;
    if (EqualsUtils.equals (m_aOrderItems, aRealOrderItems))
      return EChange.UNCHANGED;
    m_aOrderItems = aRealOrderItems;
    return EChange.CHANGED;
  }

  @Nullable
  public ProcOrderItem getOrderItemOfID (final int nID)
  {
    if (m_aOrderItems != null)
      for (final ProcOrderItem aOrderItem : m_aOrderItems)
        if (aOrderItem.getID () == nID)
          return aOrderItem;
    return null;
  }

  @Column (name = FIELD_ISSUE_DATE)
  @Convert ("joda-localdate")
  @Nullable
  public LocalDate getIssueDate ()
  {
    return m_aIssueDate;
  }

  @Nonnull
  public EChange setIssueDate (@Nullable final LocalDate aIssueDate)
  {
    if (EqualsUtils.equals (aIssueDate, m_aIssueDate))
      return EChange.UNCHANGED;
    m_aIssueDate = aIssueDate;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_DELIVERY_START_DATE)
  @Convert ("joda-localdate")
  @Nullable
  public LocalDate getDeliveryStartDate ()
  {
    return m_aDeliveryStartDate;
  }

  @Nonnull
  public EChange setDeliveryStartDate (@Nullable final LocalDate aDeliveryStartDate)
  {
    if (EqualsUtils.equals (aDeliveryStartDate, m_aDeliveryStartDate))
      return EChange.UNCHANGED;
    m_aDeliveryStartDate = aDeliveryStartDate;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_DELIVERY_END_DATE)
  @Convert ("joda-localdate")
  @Nullable
  public LocalDate getDeliveryEndDate ()
  {
    return m_aDeliveryEndDate;
  }

  @Nonnull
  public EChange setDeliveryEndDate (@Nullable final LocalDate aDeliveryEndDate)
  {
    if (EqualsUtils.equals (m_aDeliveryEndDate, aDeliveryEndDate))
      return EChange.UNCHANGED;
    m_aDeliveryEndDate = aDeliveryEndDate;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_INCOTERM)
  @Nullable
  public EIncoterm getIncoterm ()
  {
    return m_eIncoterm;
  }

  @Nonnull
  public EChange setIncoterm (@Nullable final EIncoterm eIncoterm)
  {
    if (EqualsUtils.equals (m_eIncoterm, eIncoterm))
      return EChange.UNCHANGED;
    m_eIncoterm = eIncoterm;
    return EChange.CHANGED;
  }

  @OneToMany (mappedBy = ProcOrderAttachment.FIELD_OWNER, cascade = CascadeType.ALL)
  @JoinColumn (name = FIELD_ATTACHMENTS)
  @ReturnsMutableObject (reason = "EclipseLink managed object")
  @Nullable
  public List <ProcOrderAttachment> getAttachments ()
  {
    return m_aAttachments;
  }

  @Nonnull
  public EChange setAttachments (@Nullable final List <ProcOrderAttachment> aAttachments)
  {
    final List <ProcOrderAttachment> aRealAttachments = ContainerHelper.isEmpty (aAttachments) ? null : aAttachments;
    if (EqualsUtils.equals (m_aAttachments, aRealAttachments))
      return EChange.UNCHANGED;
    m_aAttachments = aRealAttachments;
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllAttachmentIDs ()
  {
    final Set <String> ret = new HashSet <String> ();
    if (m_aAttachments != null)
      for (final IProcOrderAttachment aAttachment : m_aAttachments)
        ret.add (aAttachment.getAttachmentID ());
    return ret;
  }

  @Nullable
  public ProcOrderAttachment getAttachmentOfID (final int nID)
  {
    if (m_aAttachments != null)
      for (final ProcOrderAttachment aAttachment : m_aAttachments)
        if (aAttachment.getID () == nID)
          return aAttachment;
    return null;
  }

  private static ICurrencyValue _addOrCreate (@Nullable final ICurrencyValue aBaseValue,
                                              @Nullable final IReadonlyCurrencyValue aAddValue)
  {
    if (aAddValue == null)
      return aBaseValue;
    if (aBaseValue == null)
      return new CurrencyValue (aAddValue);
    return aBaseValue.getAdded (aAddValue.getValue ());
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalNetAmount ()
  {
    ICurrencyValue aValue = null;
    if (m_aOrderItems != null)
      for (final IProcOrderItem aItem : m_aOrderItems)
        aValue = _addOrCreate (aValue, aItem.getTotalNetAmount ());
    return aValue;
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalGrossAmount ()
  {
    ICurrencyValue aValue = null;
    if (m_aOrderItems != null)
      for (final IProcOrderItem aItem : m_aOrderItems)
        aValue = _addOrCreate (aValue, aItem.getTotalGrossAmount ());
    return aValue;
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalTaxAmount ()
  {
    ICurrencyValue aValue = null;
    if (m_aOrderItems != null)
      for (final IProcOrderItem aItem : m_aOrderItems)
        aValue = _addOrCreate (aValue, aItem.getTotalTaxAmount ());
    return aValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcOrder rhs = (ProcOrder) o;
    return EqualsUtils.equals (m_sOrderNumber, rhs.m_sOrderNumber) &&
           EqualsUtils.equals (m_aOrderItems, rhs.m_aOrderItems) &&
           EqualsUtils.equals (m_aIssueDate, rhs.m_aIssueDate) &&
           EqualsUtils.equals (m_aDeliveryStartDate, rhs.m_aDeliveryStartDate) &&
           EqualsUtils.equals (m_aDeliveryEndDate, rhs.m_aDeliveryEndDate) &&
           EqualsUtils.equals (m_eIncoterm, rhs.m_eIncoterm) &&
           EqualsUtils.equals (m_aAttachments, rhs.m_aAttachments);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_sOrderNumber)
                            .append (m_aOrderItems)
                            .append (m_aIssueDate)
                            .append (m_aDeliveryStartDate)
                            .append (m_aDeliveryEndDate)
                            .append (m_eIncoterm)
                            .append (m_aAttachments)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("orderNumber", m_sOrderNumber)
                            .append ("orderItems", m_aOrderItems)
                            .append ("issueDate", m_aIssueDate)
                            .append ("deliveryStartDate", m_aDeliveryStartDate)
                            .append ("deliveryEndDate", m_aDeliveryEndDate)
                            .append ("incoterm", m_eIncoterm)
                            .append ("attachments", m_aAttachments)
                            .toString ();
  }
}
