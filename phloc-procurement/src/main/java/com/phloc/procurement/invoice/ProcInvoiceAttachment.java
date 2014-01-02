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
package com.phloc.procurement.invoice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.phloc.commons.CGlobal;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.masterdata.MasterdataUtils;
import com.phloc.procurement.attachment.IProcAttachment;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.CProcLengthConstraints;

/**
 * Represents a single invoice attachment.
 * 
 * @author Philip Helger
 */
@Entity
@Table (name = "invoiceattach")
@Access (value = AccessType.PROPERTY)
public final class ProcInvoiceAttachment extends AbstractProcObject implements IProcInvoiceAttachment
{
  public static final String FIELD_OWNER = "owner";
  public static final String FIELD_ATTACHMENTID = "attachmentid";
  public static final int LENGTH_ATTACHMENTID = CProcLengthConstraints.LENGTH_TEXT;

  private ProcInvoice m_aOwner;
  private String m_sAttachmentID;

  public ProcInvoiceAttachment ()
  {}

  public ProcInvoiceAttachment (@Nonnull final ProcInvoice aOwner)
  {
    setOwner (aOwner);
  }

  public ProcInvoiceAttachment (@Nonnull final ProcInvoice aOwner, @Nonnull final IProcAttachment aAttachment)
  {
    setOwner (aOwner);
    setAttachmentID (aAttachment.getID ());
  }

  @ManyToOne
  @JoinColumn (name = FIELD_OWNER, nullable = false)
  @Nullable
  public ProcInvoice getOwner ()
  {
    return m_aOwner;
  }

  public void setOwner (@Nonnull final ProcInvoice aOwner)
  {
    if (aOwner == null)
      throw new NullPointerException ("owner");
    m_aOwner = aOwner;
  }

  @Column (name = FIELD_ATTACHMENTID, length = LENGTH_ATTACHMENTID)
  @Nullable
  public String getAttachmentID ()
  {
    return m_sAttachmentID;
  }

  @Nonnull
  public EChange setAttachmentID (@Nullable final String sAttachmentID)
  {
    final String sRealAttachmentID = MasterdataUtils.getEnsuredLength (sAttachmentID, LENGTH_ATTACHMENTID);
    if (EqualsUtils.equals (sRealAttachmentID, m_sAttachmentID))
      return EChange.UNCHANGED;
    m_sAttachmentID = sRealAttachmentID;
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
    final ProcInvoiceAttachment rhs = (ProcInvoiceAttachment) o;
    return getOwnerID () == rhs.getOwnerID () && EqualsUtils.equals (m_sAttachmentID, rhs.m_sAttachmentID);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (getOwnerID ())
                            .append (m_sAttachmentID)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ownerID", getOwnerID ())
                            .append ("attachmentID", m_sAttachmentID)
                            .toString ();
  }
}
