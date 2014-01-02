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

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.masterdata.price.ReadonlyPrice;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.procurement.codelist.ECommodityScheme;

public final class ProcInvoiceLineMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_ORDERITEMID = "orderitemid";
  private static final String ATTR_LINEID = "lineid";
  private static final String ATTR_COMMODITY_SCHEME = "commodityscheme";
  private static final String ATTR_COMMODITY_ID = "commodityid";

  private static final String ELEMENT_DESCRIPTION = "description";
  private static final String ATTR_QUANTITY = "quantity";
  private static final String ELEMENT_PRICE = "price";
  private static final String ATTR_TAX_CATEGORY = "taxcategory";
  private static final String ELEMENT_REMARK = "remark";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IProcInvoiceLine aValue = (IProcInvoiceLine) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_ORDERITEMID, aValue.getOrderItemID ());
    aElement.setAttribute (ATTR_LINEID, aValue.getLineID ());
    aElement.setAttribute (ATTR_COMMODITY_SCHEME, aValue.getCommoditySchemeID ());
    aElement.setAttribute (ATTR_COMMODITY_ID, aValue.getCommodityID ());

    if (StringHelper.hasText (aValue.getDescription ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_DESCRIPTION).appendText (aValue.getDescription ());
    aElement.setAttribute (ATTR_QUANTITY, aValue.getQuantity ());
    aElement.appendChild (MicroTypeConverter.convertToMicroElement (aValue.getUnitPrice (),
                                                                    sNamespaceURI,
                                                                    ELEMENT_PRICE));
    aElement.setAttribute (ATTR_TAX_CATEGORY, aValue.getTaxCategoryID ());
    if (StringHelper.hasText (aValue.getRemark ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_REMARK).appendText (aValue.getRemark ());
    return aElement;
  }

  @Nonnull
  public ProcInvoiceLine convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcInvoiceLine ret = new ProcInvoiceLine ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    ret.setOrderItemID (aElement.getAttribute (ATTR_ORDERITEMID));
    ret.setLineID (aElement.getAttribute (ATTR_LINEID));
    ret.setCommodityScheme (ECommodityScheme.getFromIDOrNull (aElement.getAttribute (ATTR_COMMODITY_SCHEME)));
    ret.setCommodityID (aElement.getAttribute (ATTR_COMMODITY_ID));
    ret.setDescription (MicroUtils.getChildTextContent (aElement, ELEMENT_DESCRIPTION));
    ret.setQuantity (StringParser.parseInt (aElement.getAttribute (ATTR_QUANTITY), CGlobal.ILLEGAL_UINT));
    ret.setUnitPrice (MicroTypeConverter.convertToNative (aElement.getFirstChildElement (ELEMENT_PRICE),
                                                          ReadonlyPrice.class));
    ret.setTaxCategory (ETaxCategoryUN5305.getFromIDOrNull (aElement.getAttribute (ATTR_TAX_CATEGORY)));
    ret.setRemark (MicroUtils.getChildTextContent (aElement, ELEMENT_REMARK));
    return ret;
  }
}
