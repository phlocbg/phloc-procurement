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
package com.phloc.procurement.party;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.masterdata.MasterdataUtils;
import com.phloc.procurement.domain.CProcLengthConstraints;

/**
 * Domain object for the master data.
 * 
 * @author Philip Helger
 */
@Entity
@Table (name = "masterdata")
@Access (value = AccessType.PROPERTY)
public final class ProcMasterData extends AbstractProcParty implements IProcMasterData
{
  public static final String FIELD_CLIENT = "client";
  public static final String FIELD_EMAIL = "email";
  public static final int LENGTH_EMAIL = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_TELEPHONE = "telephone";
  public static final int LENGTH_TELEPHONE = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_FIRSTNAME = "firstname";
  public static final int LENGTH_FIRSTNAME = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_LASTNAME = "lastname";
  public static final int LENGTH_LASTNAME = CProcLengthConstraints.LENGTH_TEXT;

  private int m_nClient;
  private String m_sEmail;
  private String m_sTelephone;
  private String m_sFirstName;
  private String m_sLastName;

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

  @Column (name = FIELD_EMAIL, length = LENGTH_EMAIL)
  @Nullable
  public String getEmail ()
  {
    return m_sEmail;
  }

  @Nonnull
  public EChange setEmail (@Nullable final String sEmail)
  {
    final String sRealEmail = MasterdataUtils.getEnsuredLength (sEmail, LENGTH_EMAIL);
    if (EqualsUtils.equals (sRealEmail, m_sEmail))
      return EChange.UNCHANGED;
    m_sEmail = sRealEmail;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_TELEPHONE, length = LENGTH_TELEPHONE)
  @Nullable
  public String getTelephone ()
  {
    return m_sTelephone;
  }

  @Nonnull
  public EChange setTelephone (@Nullable final String sTelephone)
  {
    final String sRealTelephone = MasterdataUtils.getEnsuredLength (sTelephone, LENGTH_TELEPHONE);
    if (EqualsUtils.equals (sRealTelephone, m_sTelephone))
      return EChange.UNCHANGED;
    m_sTelephone = sRealTelephone;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_FIRSTNAME, length = LENGTH_FIRSTNAME)
  @Nullable
  public String getFirstName ()
  {
    return m_sFirstName;
  }

  @Nonnull
  public EChange setFirstName (@Nullable final String sFirstName)
  {
    final String sRealFirstName = MasterdataUtils.getEnsuredLength (sFirstName, LENGTH_FIRSTNAME);
    if (EqualsUtils.equals (sRealFirstName, m_sFirstName))
      return EChange.UNCHANGED;
    m_sFirstName = sRealFirstName;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_LASTNAME, length = LENGTH_LASTNAME)
  @Nullable
  public String getLastName ()
  {
    return m_sLastName;
  }

  @Nonnull
  public EChange setLastName (@Nullable final String sLastName)
  {
    final String sRealLastName = MasterdataUtils.getEnsuredLength (sLastName, LENGTH_LASTNAME);
    if (EqualsUtils.equals (sRealLastName, m_sLastName))
      return EChange.UNCHANGED;
    m_sLastName = sRealLastName;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcMasterData rhs = (ProcMasterData) o;
    return m_nClient == rhs.m_nClient &&
           EqualsUtils.equals (m_sEmail, rhs.m_sEmail) &&
           EqualsUtils.equals (m_sTelephone, rhs.m_sTelephone) &&
           EqualsUtils.equals (m_sFirstName, rhs.m_sFirstName) &&
           EqualsUtils.equals (m_sLastName, rhs.m_sLastName);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_nClient)
                            .append (m_sEmail)
                            .append (m_sTelephone)
                            .append (m_sFirstName)
                            .append (m_sLastName)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("client", m_nClient)
                            .append ("email", m_sEmail)
                            .append ("telephone", m_sTelephone)
                            .append ("firstName", m_sFirstName)
                            .append ("lastName", m_sLastName)
                            .toString ();
  }
}
