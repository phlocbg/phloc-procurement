package com.phloc.procurement.invoice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringParser;

public final class ProcInvoiceAttachmentMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_ATTACHMENTID = "attachmentid";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IProcInvoiceAttachment aValue = (IProcInvoiceAttachment) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_ATTACHMENTID, aValue.getAttachmentID ());
    return aElement;
  }

  @Nonnull
  public ProcInvoiceAttachment convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcInvoiceAttachment ret = new ProcInvoiceAttachment ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    ret.setAttachmentID (aElement.getAttribute (ATTR_ATTACHMENTID));
    return ret;
  }
}
