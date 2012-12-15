package com.phloc.procurement.party;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;

public final class ProcMasterDataMicroTypeConverter extends AbstractProcPartyMicroTypeConverter
{
  private static final String ATTR_CLIENT = "client";
  private static final String ATTR_EMAIL = "email";
  private static final String ATTR_TELEPHONE = "telephone";
  private static final String ATTR_FIRSTNAME = "firstname";
  private static final String ATTR_LASTNAME = "lastname";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final ProcMasterData aValue = (ProcMasterData) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    convertToMicroElementPartial (aValue, aElement);
    aElement.setAttribute (ATTR_CLIENT, aValue.getClient ());
    if (StringHelper.hasText (aValue.getEmail ()))
      aElement.setAttribute (ATTR_EMAIL, aValue.getEmail ());
    if (StringHelper.hasText (aValue.getTelephone ()))
      aElement.setAttribute (ATTR_TELEPHONE, aValue.getTelephone ());
    if (StringHelper.hasText (aValue.getFirstName ()))
      aElement.setAttribute (ATTR_FIRSTNAME, aValue.getFirstName ());
    if (StringHelper.hasText (aValue.getLastName ()))
      aElement.setAttribute (ATTR_LASTNAME, aValue.getLastName ());
    return aElement;
  }

  @Nonnull
  public ProcMasterData convertToNative (@Nonnull final IMicroElement aElement)
  {
    final ProcMasterData ret = new ProcMasterData ();
    convertToNativePartial (ret, aElement);
    ret.setClient (StringParser.parseInt (aElement.getAttribute (ATTR_CLIENT), CGlobal.ILLEGAL_UINT));
    ret.setEmail (aElement.getAttribute (ATTR_EMAIL));
    ret.setTelephone (aElement.getAttribute (ATTR_TELEPHONE));
    ret.setFirstName (aElement.getAttribute (ATTR_FIRSTNAME));
    ret.setLastName (aElement.getAttribute (ATTR_LASTNAME));
    return ret;
  }
}
