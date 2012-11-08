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

import java.util.List;

import javax.annotation.Nonnegative;
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

import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * The domain object for a customer.
 * 
 * @author philip
 */
@Entity
@Table (name = "customer")
@Access (value = AccessType.PROPERTY)
public final class ProcCustomer extends AbstractProcParty implements IProcCustomer
{
  public static final String FIELD_CLIENT = "client";
  public static final String FIELD_PAYMENTTERMS = "paymentterms";

  private int m_nClient;
  private List <ProcCustomerPaymentTerm> m_aPaymentTerms;

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

  @OneToMany (mappedBy = "owner", cascade = CascadeType.ALL)
  @JoinColumn (name = FIELD_PAYMENTTERMS)
  @ReturnsMutableObject (reason = "EclipseLink managed object")
  @Nullable
  public List <ProcCustomerPaymentTerm> getPaymentTerms ()
  {
    return m_aPaymentTerms;
  }

  @Nonnull
  public EChange setPaymentTerms (@Nonnull final List <ProcCustomerPaymentTerm> aPaymentTerms)
  {
    final List <ProcCustomerPaymentTerm> aRealPaymentTerms = ContainerHelper.isEmpty (aPaymentTerms) ? null
                                                                                                    : aPaymentTerms;
    if (EqualsUtils.equals (m_aPaymentTerms, aRealPaymentTerms))
      return EChange.UNCHANGED;
    m_aPaymentTerms = aRealPaymentTerms;
    return EChange.CHANGED;
  }

  @Nullable
  public ProcCustomerPaymentTerm getPaymentTermOfID (final int nID)
  {
    if (m_aPaymentTerms != null)
      for (final ProcCustomerPaymentTerm aPaymentTerm : m_aPaymentTerms)
        if (aPaymentTerm.getID () == nID)
          return aPaymentTerm;
    return null;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcCustomer rhs = (ProcCustomer) o;
    return m_nClient == rhs.m_nClient && EqualsUtils.equals (m_aPaymentTerms, rhs.m_aPaymentTerms);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_nClient).append (m_aPaymentTerms).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("client", m_nClient)
                            .append ("paymentTerms", m_aPaymentTerms)
                            .toString ();
  }
}
