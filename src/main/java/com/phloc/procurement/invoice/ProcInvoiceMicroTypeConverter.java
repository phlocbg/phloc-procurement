package com.phloc.procurement.invoice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.procurement.domain.AbstractProcDeletableObject;

public final class ProcInvoiceMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_DELETED = "deleted";
  private static final String ATTR_ISSUE_DATE = "issuedate";
  private static final String ATTR_INVOICE_NUMBER = "invoicenumber";
  private static final String ELEMENT_INVOICE_NOTE = "invoicenote";
  private static final String ELEMENT_LINE = "line";
  private static final String ELEMENT_ATTACHMENT = "attachment";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IProcInvoice aValue = (IProcInvoice) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_DELETED, Boolean.toString (aValue.isDeleted ()));
    aElement.setAttributeWithConversion (ATTR_ISSUE_DATE, aValue.getIssueDate ());
    aElement.setAttribute (ATTR_INVOICE_NUMBER, aValue.getInvoiceNumber ());
    if (StringHelper.hasText (aValue.getInvoiceNote ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_INVOICE_NOTE).appendText (aValue.getInvoiceNote ());
    final List <? extends IProcInvoiceLine> aInvoiceLines = aValue.getInvoiceLines ();
    if (aInvoiceLines != null)
      for (final IProcInvoiceLine aInvoiceLine : aInvoiceLines)
        aElement.appendChild (MicroTypeConverter.convertToMicroElement (aInvoiceLine, sNamespaceURI, ELEMENT_LINE));
    final List <? extends IProcInvoiceAttachment> aAttachments = aValue.getAttachments ();
    if (aAttachments != null)
      for (final IProcInvoiceAttachment aAttachment : aAttachments)
        aElement.appendChild (MicroTypeConverter.convertToMicroElement (aAttachment, sNamespaceURI, ELEMENT_ATTACHMENT));
    return aElement;
  }

  @Nonnull
  public ProcInvoice convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcInvoice ret = new ProcInvoice ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    ret.setDeleted (StringParser.parseBool (aElement.getAttribute (ATTR_DELETED),
                                            AbstractProcDeletableObject.DEFAULT_DELETED));
    ret.setIssueDate (aElement.getAttributeWithConversion (ATTR_ISSUE_DATE, LocalDate.class));
    ret.setInvoiceNumber (aElement.getAttribute (ATTR_INVOICE_NUMBER));
    ret.setInvoiceNote (MicroUtils.getChildTextContent (aElement, ELEMENT_INVOICE_NOTE));
    final List <ProcInvoiceLine> aInvoiceLines = new ArrayList <ProcInvoiceLine> ();
    for (final IMicroElement eInvoiceLine : aElement.getChildElements (ELEMENT_LINE))
    {
      final ProcInvoiceLine aInvoiceLine = MicroTypeConverter.convertToNative (eInvoiceLine, ProcInvoiceLine.class);
      aInvoiceLine.setOwner (ret);
      aInvoiceLines.add (aInvoiceLine);
    }
    ret.setInvoiceLines (aInvoiceLines);
    final List <ProcInvoiceAttachment> aAttachments = new ArrayList <ProcInvoiceAttachment> ();
    for (final IMicroElement eAttachment : aElement.getChildElements (ELEMENT_ATTACHMENT))
    {
      final ProcInvoiceAttachment aAttachment = MicroTypeConverter.convertToNative (eAttachment,
                                                                                    ProcInvoiceAttachment.class);
      aAttachment.setOwner (ret);
      aAttachments.add (aAttachment);
    }
    ret.setAttachments (aAttachments);
    return ret;
  }
}
