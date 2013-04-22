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
package com.phloc.procurement.order;

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
import com.phloc.procurement.party.ProcCustomer;

/**
 * Domain object for an incoming order.
 * 
 * @author Philip Helger
 */
@Entity
@Table (name = "orders_in")
@Access (value = AccessType.PROPERTY)
@Converter (name = "joda-datetime", converterClass = JPAJodaDateTimeConverter.class)
public final class ProcOrderIncoming extends AbstractProcObject implements IProcOrderIncoming
{
  public static final String FIELD_CLIENT = "client";
  public static final String FIELD_CUSTOMER = "customer";
  // remember that "order" is an SQL keyword
  public static final String FIELD_ORDER = "mainorder";
  public static final String FIELD_STATE = "state";
  public static final String FIELD_RECEIVEDT = "receivedt";

  private int m_nClient;
  /** The customer where the order is placed */
  private ProcCustomer m_aCustomer;
  private ProcOrder m_aOrder;
  private EProcState m_eState;
  private DateTime m_aReceiveDT;

  public ProcOrderIncoming ()
  {}

  public ProcOrderIncoming (@Nonnull final ProcCustomer aCustomer)
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
  @JoinColumn (name = FIELD_ORDER)
  @Nullable
  public ProcOrder getOrder ()
  {
    return m_aOrder;
  }

  @Nonnull
  public ProcOrder getOrCreateOrder ()
  {
    if (m_aOrder == null)
      m_aOrder = new ProcOrder ();
    return m_aOrder;
  }

  public void setOrder (@Nonnull final ProcOrder aOrder)
  {
    if (aOrder == null)
      throw new NullPointerException ("order");
    m_aOrder = aOrder;
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
    final ProcOrderIncoming rhs = (ProcOrderIncoming) o;
    return m_nClient == rhs.m_nClient &&
           EqualsUtils.equals (m_aCustomer, rhs.m_aCustomer) &&
           EqualsUtils.equals (m_aOrder, rhs.m_aOrder) &&
           EqualsUtils.equals (m_eState, rhs.m_eState) &&
           EqualsUtils.equals (m_aReceiveDT, rhs.m_aReceiveDT);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_nClient)
                            .append (m_aCustomer)
                            .append (m_aOrder)
                            .append (m_eState)
                            .append (m_aReceiveDT)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("client", m_nClient)
                            .append ("customer", m_aCustomer)
                            .append ("order", m_aOrder)
                            .append ("state", m_eState)
                            .append ("receiveDT", m_aReceiveDT)
                            .toString ();
  }
}
