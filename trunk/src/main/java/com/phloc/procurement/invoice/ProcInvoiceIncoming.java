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
package com.phloc.procurement.invoice;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.db.jpa.converter.JPAJodaDateTimeConverter;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.EProcState;
import com.phloc.procurement.party.ProcSupplier;

/**
 * Domain object for an incoming invoice.
 * 
 * @author philip
 */
@Entity
@Table (name = "invoices_in")
@Access (value = AccessType.PROPERTY)
@Converter (name = "joda-datetime", converterClass = JPAJodaDateTimeConverter.class)
public final class ProcInvoiceIncoming extends AbstractProcObject implements IProcInvoiceIncoming
{
  public static final String FIELD_CLIENT = "client";
  public static final String FIELD_SUPPLIER = "supplier";
  public static final String FIELD_INVOICE = "maininvoice";
  public static final String FIELD_STATE = "state";
  public static final String FIELD_RECEIVEDT = "receivedt";

  private int m_nClient;
  /** The supplier where the order is placed */
  private ProcSupplier m_aSupplier;
  private ProcInvoice m_aInvoice;
  private EProcState m_eState;
  private DateTime m_aReceiveDT;

  public ProcInvoiceIncoming ()
  {}

  public ProcInvoiceIncoming (@Nonnull final ProcSupplier aSupplier)
  {
    setSupplier (aSupplier);
  }

  @Column (name = FIELD_CLIENT, nullable = false)
  @Nonnegative
  public int getClient ()
  {
    return m_nClient;
  }

  @Nonnull
  public EChange setClient (@Nonnegative final int nClient)
  {
    if (nClient < 0)
      throw new IllegalArgumentException ("email");

    if (nClient == m_nClient)
      return EChange.UNCHANGED;
    m_nClient = nClient;
    return EChange.CHANGED;
  }

  @ManyToOne
  @JoinColumn (name = FIELD_SUPPLIER)
  @Nullable
  public ProcSupplier getSupplier ()
  {
    return m_aSupplier;
  }

  public void setSupplier (@Nonnull final ProcSupplier aSupplier)
  {
    if (aSupplier == null)
      throw new NullPointerException ("supplier");
    m_aSupplier = aSupplier;
  }

  @OneToOne (cascade = CascadeType.ALL)
  @JoinColumn (name = FIELD_INVOICE)
  @Nullable
  public ProcInvoice getInvoice ()
  {
    return m_aInvoice;
  }

  @Nonnull
  public ProcInvoice getOrCreateInvoice ()
  {
    if (m_aInvoice == null)
      m_aInvoice = new ProcInvoice ();
    return m_aInvoice;
  }

  public void setInvoice (@Nonnull final ProcInvoice aInvoice)
  {
    if (aInvoice == null)
      throw new NullPointerException ("invoice");
    m_aInvoice = aInvoice;
  }

  @Column (name = FIELD_STATE, nullable = false)
  @Nullable
  public EProcState getState ()
  {
    return m_eState;
  }

  @Nonnull
  public EChange setState (@Nonnull final EProcState eState)
  {
    if (EqualsUtils.equals (m_eState, eState))
      return EChange.UNCHANGED;
    m_eState = eState;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_RECEIVEDT)
  @Convert ("joda-datetime")
  @Nullable
  public DateTime getReceiveDateTime ()
  {
    return m_aReceiveDT;
  }

  @Nonnull
  public EChange setReceiveDateTime (@Nonnull final DateTime aReceiveDT)
  {
    if (EqualsUtils.equals (m_aReceiveDT, aReceiveDT))
      return EChange.UNCHANGED;
    m_aReceiveDT = aReceiveDT;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcInvoiceIncoming rhs = (ProcInvoiceIncoming) o;
    return m_nClient == rhs.m_nClient &&
           EqualsUtils.equals (m_aSupplier, rhs.m_aSupplier) &&
           EqualsUtils.equals (m_aInvoice, rhs.m_aInvoice) &&
           EqualsUtils.equals (m_eState, rhs.m_eState) &&
           EqualsUtils.equals (m_aReceiveDT, rhs.m_aReceiveDT);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_nClient)
                            .append (m_aSupplier)
                            .append (m_aInvoice)
                            .append (m_eState)
                            .append (m_aReceiveDT)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("client", m_nClient)
                            .append ("supplier", m_aSupplier)
                            .append ("invoice", m_aInvoice)
                            .append ("state", m_eState)
                            .append ("receiveDT", m_aReceiveDT)
                            .toString ();
  }
}
