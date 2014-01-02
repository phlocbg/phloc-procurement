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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;

public final class ProcCustomerPaymentTermMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_OWNERID = "ownerid";
  private static final String ATTR_PERCENTAGE = "percentage";
  private static final String ELEMENT_DESCRIPTION = "description";
  private static final String ATTR_DAYS = "days";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final ProcCustomerPaymentTerm aValue = (ProcCustomerPaymentTerm) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_OWNERID, aValue.getOwnerID ());
    aElement.setAttribute (ATTR_PERCENTAGE, Double.toString (aValue.getPercentage ()));
    if (StringHelper.hasText (aValue.getDescription ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_DESCRIPTION).appendText (aValue.getDescription ());
    aElement.setAttribute (ATTR_DAYS, aValue.getDays ());
    return aElement;
  }

  @Nonnull
  public ProcCustomerPaymentTerm convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcCustomerPaymentTerm ret = new ProcCustomerPaymentTerm ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    // owner is set by parent
    ret.setPercentage (StringParser.parseDouble (aElement.getAttribute (ATTR_PERCENTAGE), CGlobal.ILLEGAL_DOUBLE));
    ret.setDescription (MicroUtils.getChildTextContent (aElement, ELEMENT_DESCRIPTION));
    ret.setDays (StringParser.parseInt (aElement.getAttribute (ATTR_DAYS), CGlobal.ILLEGAL_UINT));
    return ret;
  }
}
