package com.phloc.procurement.order;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringParser;

public final class ProcOrderAttachmentMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_ATTACHMENTID = "attachmentid";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IProcOrderAttachment aValue = (IProcOrderAttachment) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_ATTACHMENTID, aValue.getAttachmentID ());
    return aElement;
  }

  @Nonnull
  public ProcOrderAttachment convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcOrderAttachment ret = new ProcOrderAttachment ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    ret.setAttachmentID (aElement.getAttribute (ATTR_ATTACHMENTID));
    return ret;
  }
}
