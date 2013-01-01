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
package com.phloc.procurement.order;

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
import com.phloc.commons.string.StringParser;
import com.phloc.masterdata.trade.EIncoterm;
import com.phloc.procurement.domain.AbstractProcDeletableObject;

public final class ProcOrderMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_DELETED = "deleted";
  private static final String ATTR_ORDER_NUMBER = "ordernumber";
  private static final String ATTR_ISSUE_DATE = "issuedate";
  private static final String ATTR_DELIVERY_START_DATE = "deliverystartdate";
  private static final String ATTR_DELIVERY_END_DATE = "deliveryenddate";
  private static final String ATTR_INCOTERM = "incoterm";
  private static final String ELEMENT_ITEM = "item";
  private static final String ELEMENT_ATTACHMENT = "attachment";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IProcOrder aValue = (IProcOrder) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_DELETED, Boolean.toString (aValue.isDeleted ()));
    aElement.setAttribute (ATTR_ORDER_NUMBER, aValue.getOrderNumber ());
    aElement.setAttributeWithConversion (ATTR_ISSUE_DATE, aValue.getIssueDate ());
    aElement.setAttributeWithConversion (ATTR_DELIVERY_START_DATE, aValue.getDeliveryStartDate ());
    aElement.setAttributeWithConversion (ATTR_DELIVERY_END_DATE, aValue.getDeliveryEndDate ());
    aElement.setAttribute (ATTR_INCOTERM, aValue.getIncotermID ());
    final List <? extends IProcOrderItem> aOrderItems = aValue.getOrderItems ();
    if (aOrderItems != null)
      for (final IProcOrderItem aOrderItem : aOrderItems)
        aElement.appendChild (MicroTypeConverter.convertToMicroElement (aOrderItem, sNamespaceURI, ELEMENT_ITEM));
    final List <? extends IProcOrderAttachment> aAttachments = aValue.getAttachments ();
    if (aAttachments != null)
      for (final IProcOrderAttachment aAttachment : aAttachments)
        aElement.appendChild (MicroTypeConverter.convertToMicroElement (aAttachment, sNamespaceURI, ELEMENT_ATTACHMENT));
    return aElement;
  }

  @Nonnull
  public ProcOrder convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcOrder ret = new ProcOrder ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    ret.setDeleted (StringParser.parseBool (aElement.getAttribute (ATTR_DELETED),
                                            AbstractProcDeletableObject.DEFAULT_DELETED));
    ret.setOrderNumber (aElement.getAttribute (ATTR_ORDER_NUMBER));
    ret.setIssueDate (aElement.getAttributeWithConversion (ATTR_ISSUE_DATE, LocalDate.class));
    ret.setDeliveryStartDate (aElement.getAttributeWithConversion (ATTR_DELIVERY_START_DATE, LocalDate.class));
    ret.setDeliveryEndDate (aElement.getAttributeWithConversion (ATTR_DELIVERY_END_DATE, LocalDate.class));
    ret.setIncoterm (EIncoterm.getFromIDOrNull (aElement.getAttribute (ATTR_INCOTERM)));
    final List <ProcOrderItem> aOrderItems = new ArrayList <ProcOrderItem> ();
    for (final IMicroElement eOrderItem : aElement.getChildElements (ELEMENT_ITEM))
    {
      final ProcOrderItem aOrderItem = MicroTypeConverter.convertToNative (eOrderItem, ProcOrderItem.class);
      aOrderItem.setOwner (ret);
      aOrderItems.add (aOrderItem);
    }
    ret.setOrderItems (aOrderItems);
    final List <ProcOrderAttachment> aAttachments = new ArrayList <ProcOrderAttachment> ();
    for (final IMicroElement eAttachment : aElement.getChildElements (ELEMENT_ATTACHMENT))
    {
      final ProcOrderAttachment aAttachment = MicroTypeConverter.convertToNative (eAttachment,
                                                                                  ProcOrderAttachment.class);
      aAttachment.setOwner (ret);
      aAttachments.add (aAttachment);
    }
    ret.setAttachments (aAttachments);
    return ret;
  }
}
