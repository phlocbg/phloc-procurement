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

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.EProcState;
import com.phloc.procurement.party.ProcCustomer;

/**
 * Domain object for an outgoing invoice.
 * 
 * @author philip
 */
@Entity
@Table (name = "invoices_out")
@Access (value = AccessType.PROPERTY)
public final class ProcInvoiceOutgoing extends AbstractProcObject implements IProcInvoiceOutgoing
{
  public static final String FIELD_CLIENT = "client";
  public static final String FIELD_CUSTOMER = "customer";
  public static final String FIELD_INVOICE = "maininvoice";
  public static final String FIELD_STATE = "state";

  private int m_nClient;
  /** The customer where the invoice is placed */
  private ProcCustomer m_aCustomer;
  private ProcInvoice m_aInvoice;
  private EProcState m_eState;

  public ProcInvoiceOutgoing ()
  {}

  public ProcInvoiceOutgoing (@Nonnull final ProcCustomer aCustomer)
  {
    setCustomer (aCustomer);
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
  @JoinColumn (name = FIELD_CUSTOMER)
  @Nullable
  public ProcCustomer getCustomer ()
  {
    return m_aCustomer;
  }

  public void setCustomer (@Nonnull final ProcCustomer aCustomer)
  {
    if (aCustomer == null)
      throw new NullPointerException ("customer");
    m_aCustomer = aCustomer;
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

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcInvoiceOutgoing rhs = (ProcInvoiceOutgoing) o;
    return m_nClient == rhs.m_nClient &&
           EqualsUtils.equals (m_aCustomer, rhs.m_aCustomer) &&
           EqualsUtils.equals (m_aInvoice, rhs.m_aInvoice) &&
           EqualsUtils.equals (m_eState, rhs.m_eState);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_nClient)
                            .append (m_aCustomer)
                            .append (m_aInvoice)
                            .append (m_eState)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("client", m_nClient)
                            .append ("customer", m_aCustomer)
                            .append ("invoice", m_aInvoice)
                            .append ("state", m_eState)
                            .toString ();
  }
}
