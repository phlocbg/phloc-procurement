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
package com.phloc.procurement.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Base class for Ebiz domain objects.
 * 
 * @author philip
 */
@MappedSuperclass
@Access (value = AccessType.PROPERTY)
public abstract class AbstractProcObject implements IProcObject
{
  public static final String FIELD_ID = "id";

  private int m_nID;

  /**
   * @return Get the ID of this object. This ID is the primary ID column in the
   *         database.
   */
  @Column (name = FIELD_ID)
  @Id
  @GeneratedValue
  public final int getID ()
  {
    return m_nID;
  }

  public final void setID (final int nID)
  {
    m_nID = nID;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractProcObject rhs = (AbstractProcObject) o;
    return m_nID == rhs.m_nID;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("id", m_nID).toString ();
  }
}
