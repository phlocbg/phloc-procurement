package com.phloc.procurement.party;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringParser;

public final class ProcCustomerMicroTypeConverter extends AbstractProcPartyMicroTypeConverter
{
  private static final String ATTR_CLIENT = "client";
  private static final String ELEMENT_PAYMENTTERM = "paymentterm";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final ProcCustomer aValue = (ProcCustomer) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    convertToMicroElementPartial (aValue, aElement);
    aElement.setAttribute (ATTR_CLIENT, aValue.getClient ());
    final List <ProcCustomerPaymentTerm> aPaymentTerms = aValue.getPaymentTerms ();
    if (aPaymentTerms != null)
      for (final ProcCustomerPaymentTerm aPaymentTerm : aPaymentTerms)
        aElement.appendChild (MicroTypeConverter.convertToMicroElement (aPaymentTerm,
                                                                        sNamespaceURI,
                                                                        ELEMENT_PAYMENTTERM));
    return aElement;
  }

  @Nonnull
  public ProcCustomer convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcCustomer ret = new ProcCustomer ();
    convertToNativePartial (ret, aElement);
    ret.setClient (StringParser.parseInt (aElement.getAttribute (ATTR_CLIENT), CGlobal.ILLEGAL_UINT));
    final List <ProcCustomerPaymentTerm> aPaymentTerms = new ArrayList <ProcCustomerPaymentTerm> ();
    for (final IMicroElement ePaymentTerm : aElement.getChildElements (ELEMENT_PAYMENTTERM))
    {
      final ProcCustomerPaymentTerm aPaymentTerm = MicroTypeConverter.convertToNative (ePaymentTerm,
                                                                                       ProcCustomerPaymentTerm.class);
      aPaymentTerm.setOwner (ret);
      aPaymentTerms.add (aPaymentTerm);
    }
    if (!aPaymentTerms.isEmpty ())
      ret.setPaymentTerms (aPaymentTerms);
    return ret;
  }
}
