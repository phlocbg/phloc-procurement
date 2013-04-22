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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * The domain object for the supplier.
 * 
 * @author Philip Helger
 */
@Entity
@Table (name = "supplier")
@Access (value = AccessType.PROPERTY)
public final class ProcSupplier extends AbstractProcParty implements IProcSupplier
{
  public static final String FIELD_CLIENT = "client";

  private int m_nClient;

  public ProcSupplier ()
  {}

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

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcSupplier rhs = (ProcSupplier) o;
    return m_nClient == rhs.m_nClient;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_nClient).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("client", m_nClient).toString ();
  }
}
