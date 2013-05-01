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
package com.phloc.procurement.invoice;

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
import com.phloc.db.jpa.eclipselink.converter.JPAJodaLocalDateConverter;
import com.phloc.masterdata.MasterdataUtils;
import com.phloc.masterdata.currency.CurrencyValue;
import com.phloc.masterdata.currency.ICurrencyValue;
import com.phloc.masterdata.currency.IReadonlyCurrencyValue;
import com.phloc.masterdata.currency.ReadonlyCurrencyValue;
import com.phloc.procurement.domain.AbstractProcDeletableObject;
import com.phloc.procurement.domain.CProcLengthConstraints;

@Entity
@Table (name = "invoice")
@Access (value = AccessType.PROPERTY)
@Converter (name = "joda-localdate", converterClass = JPAJodaLocalDateConverter.class)
public final class ProcInvoice extends AbstractProcDeletableObject implements IProcInvoice
{
  public static final String FIELD_ISSUEDATE = "issuedate";
  public static final String FIELD_INVOICENUMBER = "number";
  public static final int LENGTH_INVOICENUMBER = CProcLengthConstraints.LENGTH_TEXT;
  public static final String FIELD_INVOICENOTE = "invoicenote";
  public static final int LENGTH_INVOICENOTE = CProcLengthConstraints.LENGTH_TEXT_LONG;
  public static final String FIELD_INVOICE_LINES = "invoicelines";
  public static final String FIELD_ATTACHMENTS = "attachments";

  /** The issue date */
  private LocalDate m_aIssueDate;

  /** The invoice number */
  private String m_sInvoiceNumber;

  /** The invoice note */
  private String m_sInvoiceNote;

  /** The invoice lines */
  private List <ProcInvoiceLine> m_aInvoiceLines;

  /** The optional attachments */
  private List <ProcInvoiceAttachment> m_aAttachments;

  @Column (name = FIELD_ISSUEDATE)
  @Convert ("joda-localdate")
  @Nullable
  public LocalDate getIssueDate ()
  {
    return m_aIssueDate;
  }

  @Nonnull
  public EChange setIssueDate (@Nullable final LocalDate aIssueDate)
  {
    if (EqualsUtils.equals (m_aIssueDate, aIssueDate))
      return EChange.UNCHANGED;
    m_aIssueDate = aIssueDate;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_INVOICENUMBER, length = LENGTH_INVOICENUMBER)
  @Index
  @Nullable
  public String getInvoiceNumber ()
  {
    return m_sInvoiceNumber;
  }

  @Nonnull
  public EChange setInvoiceNumber (@Nullable final String sInvoiceNumber)
  {
    final String sRealInvoiceNumber = MasterdataUtils.getEnsuredLength (sInvoiceNumber, LENGTH_INVOICENUMBER);
    if (EqualsUtils.equals (m_sInvoiceNumber, sRealInvoiceNumber))
      return EChange.UNCHANGED;
    m_sInvoiceNumber = sRealInvoiceNumber;
    return EChange.CHANGED;
  }

  @Column (name = FIELD_INVOICENOTE, length = LENGTH_INVOICENOTE)
  @Nullable
  public String getInvoiceNote ()
  {
    return m_sInvoiceNote;
  }

  @Nonnull
  public EChange setInvoiceNote (@Nullable final String sInvoiceNote)
  {
    final String sRealInvoiceNote = MasterdataUtils.getEnsuredLength (sInvoiceNote, LENGTH_INVOICENOTE);
    if (EqualsUtils.equals (m_sInvoiceNote, sRealInvoiceNote))
      return EChange.UNCHANGED;
    m_sInvoiceNote = sRealInvoiceNote;
    return EChange.CHANGED;
  }

  @OneToMany (mappedBy = ProcInvoiceLine.FIELD_OWNER, cascade = CascadeType.ALL)
  @JoinColumn (name = FIELD_INVOICE_LINES)
  @ReturnsMutableObject (reason = "EclipseLink managed object")
  @Nullable
  public List <ProcInvoiceLine> getInvoiceLines ()
  {
    return m_aInvoiceLines;
  }

  @Nonnull
  public EChange setInvoiceLines (@Nullable final List <ProcInvoiceLine> aInvoiceLines)
  {
    final List <ProcInvoiceLine> aRealInvoiceLines = ContainerHelper.isEmpty (aInvoiceLines) ? null : aInvoiceLines;
    if (EqualsUtils.equals (m_aInvoiceLines, aRealInvoiceLines))
      return EChange.UNCHANGED;
    m_aInvoiceLines = aRealInvoiceLines;
    return EChange.CHANGED;
  }

  @Nullable
  public ProcInvoiceLine getInvoiceLineOfID (final int nID)
  {
    if (m_aInvoiceLines != null)
      for (final ProcInvoiceLine aLine : m_aInvoiceLines)
        if (aLine.getID () == nID)
          return aLine;
    return null;
  }

  @OneToMany (mappedBy = ProcInvoiceAttachment.FIELD_OWNER, cascade = CascadeType.ALL)
  @JoinColumn (name = FIELD_ATTACHMENTS)
  @ReturnsMutableObject (reason = "EclipseLink managed object")
  @Nullable
  public List <ProcInvoiceAttachment> getAttachments ()
  {
    return m_aAttachments;
  }

  @Nonnull
  public EChange setAttachments (@Nullable final List <ProcInvoiceAttachment> aAttachments)
  {
    final List <ProcInvoiceAttachment> aRealAttachments = ContainerHelper.isEmpty (aAttachments) ? null : aAttachments;
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
      for (final IProcInvoiceAttachment aAttachment : m_aAttachments)
        ret.add (aAttachment.getAttachmentID ());
    return ret;
  }

  @Nullable
  public ProcInvoiceAttachment getAttachmentOfID (final int nID)
  {
    if (m_aAttachments != null)
      for (final ProcInvoiceAttachment aAttachment : m_aAttachments)
        if (aAttachment.getID () == nID)
          return aAttachment;
    return null;
  }

  @Nullable
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
    ICurrencyValue aCV = null;
    if (m_aInvoiceLines != null)
      for (final IProcInvoiceLine aInvoiceLine : m_aInvoiceLines)
        aCV = _addOrCreate (aCV, aInvoiceLine.getTotalNetAmount ());
    return aCV == null ? null : new ReadonlyCurrencyValue (aCV);
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalGrossAmount ()
  {
    ICurrencyValue aCV = null;
    if (m_aInvoiceLines != null)
      for (final IProcInvoiceLine aInvoiceLine : m_aInvoiceLines)
        aCV = _addOrCreate (aCV, aInvoiceLine.getTotalGrossAmount ());
    return aCV == null ? null : new ReadonlyCurrencyValue (aCV);
  }

  @Nullable
  public IReadonlyCurrencyValue getTotalTaxAmount ()
  {
    ICurrencyValue aCV = null;
    if (m_aInvoiceLines != null)
      for (final IProcInvoiceLine aInvoiceLine : m_aInvoiceLines)
        aCV = _addOrCreate (aCV, aInvoiceLine.getTotalTaxAmount ());
    return aCV == null ? null : new ReadonlyCurrencyValue (aCV);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcInvoice rhs = (ProcInvoice) o;
    return EqualsUtils.equals (m_aIssueDate, rhs.m_aIssueDate) &&
           EqualsUtils.equals (m_sInvoiceNumber, rhs.m_sInvoiceNumber) &&
           EqualsUtils.equals (m_sInvoiceNote, rhs.m_sInvoiceNote) &&
           EqualsUtils.equals (m_aInvoiceLines, rhs.m_aInvoiceLines) &&
           EqualsUtils.equals (m_aAttachments, rhs.m_aAttachments);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_aIssueDate)
                            .append (m_sInvoiceNumber)
                            .append (m_sInvoiceNote)
                            .append (m_aInvoiceLines)
                            .append (m_aAttachments)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("issueDate", m_aIssueDate)
                            .append ("invoiceNumber", m_sInvoiceNumber)
                            .append ("invoiceNote", m_sInvoiceNote)
                            .append ("invoiceLines", m_aInvoiceLines)
                            .append ("attachments", m_aAttachments)
                            .toString ();
  }
}
