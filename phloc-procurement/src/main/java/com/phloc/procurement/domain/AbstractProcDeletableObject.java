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
package com.phloc.procurement.domain;

import javax.annotation.Nonnull;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

@MappedSuperclass
@Access (value = AccessType.PROPERTY)
public abstract class AbstractProcDeletableObject extends AbstractProcObject implements IProcDeletableObject
{
  public static final boolean DEFAULT_DELETED = false;
  public static final String FIELD_DELETED = "deleted";

  private boolean m_bDeleted = DEFAULT_DELETED;

  @Column (name = FIELD_DELETED)
  public final boolean isDeleted ()
  {
    return m_bDeleted;
  }

  @Nonnull
  public final EChange setDeleted (final boolean bDeleted)
  {
    if (bDeleted == m_bDeleted)
      return EChange.UNCHANGED;
    m_bDeleted = bDeleted;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AbstractProcDeletableObject rhs = (AbstractProcDeletableObject) o;
    return m_bDeleted == rhs.m_bDeleted;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_bDeleted).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("deleted", m_bDeleted).toString ();
  }
}
