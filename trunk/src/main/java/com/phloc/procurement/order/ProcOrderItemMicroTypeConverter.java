package com.phloc.procurement.order;

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
import com.phloc.procurement.codelist.ECommodityScheme;

public final class ProcOrderItemMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ELEMENT_DESCRIPTION = "description";
  private static final String ELEMENT_NOTE = "note";
  private static final String ATTR_QUANTITY = "quantity";
  private static final String ELEMENT_PRICE = "price";
  private static final String ATTR_COMMODITY_SCHEME = "commodityscheme";
  private static final String ATTR_COMMODITY_ID = "commodityid";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IProcOrderItem aValue = (IProcOrderItem) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    if (StringHelper.hasText (aValue.getDescription ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_DESCRIPTION).appendText (aValue.getDescription ());
    if (StringHelper.hasText (aValue.getNote ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_NOTE).appendText (aValue.getNote ());
    aElement.setAttribute (ATTR_QUANTITY, aValue.getQuantity ());
    aElement.appendChild (MicroTypeConverter.convertToMicroElement (aValue.getUnitPrice (),
                                                                    sNamespaceURI,
                                                                    ELEMENT_PRICE));
    aElement.setAttribute (ATTR_COMMODITY_SCHEME, aValue.getCommoditySchemeID ());
    aElement.setAttribute (ATTR_COMMODITY_ID, aValue.getCommodityID ());
    return aElement;
  }

  @Nonnull
  public ProcOrderItem convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcOrderItem ret = new ProcOrderItem ();
    ret.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    ret.setDescription (MicroUtils.getChildTextContent (aElement, ELEMENT_DESCRIPTION));
    ret.setNote (MicroUtils.getChildTextContent (aElement, ELEMENT_NOTE));
    ret.setQuantity (StringParser.parseInt (aElement.getAttribute (ATTR_QUANTITY), CGlobal.ILLEGAL_UINT));
    ret.setUnitPrice (MicroTypeConverter.convertToNative (aElement.getFirstChildElement (ELEMENT_PRICE),
                                                          ReadonlyPrice.class));
    ret.setCommodityScheme (ECommodityScheme.getFromIDOrNull (aElement.getAttribute (ATTR_COMMODITY_SCHEME)));
    ret.setCommodityID (aElement.getAttribute (ATTR_COMMODITY_ID));
    return ret;
  }
}
