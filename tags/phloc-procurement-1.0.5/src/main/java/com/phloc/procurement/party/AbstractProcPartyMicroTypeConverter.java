package com.phloc.procurement.party;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.procurement.domain.AbstractProcDeletableObject;
import com.phloc.procurement.idscheme.EntityIDSchemeManager;

public abstract class AbstractProcPartyMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_DELETED = "deleted";
  private static final String ATTR_ENTITYIDSCHEMEID = "entityidschemeid";
  private static final String ATTR_ENTITYID = "entityid";
  private static final String ATTR_ENDPOINTID = "endpointid";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_STREET = "street";
  private static final String ATTR_BUILDINGNUMBER = "buildingnumber";
  private static final String ATTR_POSTBOX = "postbox";
  private static final String ATTR_ZIPCODE = "zipcode";
  private static final String ATTR_CITY = "city";
  private static final String ATTR_COUNTRY = "country";
  private static final String ATTR_REGISTRATIONNAME = "registrationname";
  private static final String ATTR_COMPANYREGISTRATIONNUMBER = "companyregistrationnumber";
  private static final String ATTR_VATIN = "vatin";
  private static final String ATTR_BIC = "bic";
  private static final String ATTR_IBAN = "iban";

  @Nonnull
  protected final void convertToMicroElementPartial (@Nonnull final AbstractProcParty aValue,
                                                     @Nonnull final IMicroElement aElement)
  {
    aElement.setAttribute (ATTR_ID, aValue.getID ());
    aElement.setAttribute (ATTR_DELETED, Boolean.toString (aValue.isDeleted ()));
    if (aValue.getEntityIDScheme () != null)
      aElement.setAttribute (ATTR_ENTITYIDSCHEMEID, aValue.getEntityIDSchemeID ());
    if (StringHelper.hasText (aValue.getEntityID ()))
      aElement.setAttribute (ATTR_ENTITYID, aValue.getEntityID ());
    if (StringHelper.hasText (aValue.getEndpointID ()))
      aElement.setAttribute (ATTR_ENDPOINTID, aValue.getEndpointID ());
    if (StringHelper.hasText (aValue.getName ()))
      aElement.setAttribute (ATTR_NAME, aValue.getName ());
    if (StringHelper.hasText (aValue.getStreet ()))
      aElement.setAttribute (ATTR_STREET, aValue.getStreet ());
    if (StringHelper.hasText (aValue.getBuildingNumber ()))
      aElement.setAttribute (ATTR_BUILDINGNUMBER, aValue.getBuildingNumber ());
    if (StringHelper.hasText (aValue.getPostbox ()))
      aElement.setAttribute (ATTR_POSTBOX, aValue.getPostbox ());
    if (StringHelper.hasText (aValue.getZipCode ()))
      aElement.setAttribute (ATTR_ZIPCODE, aValue.getZipCode ());
    if (StringHelper.hasText (aValue.getCity ()))
      aElement.setAttribute (ATTR_CITY, aValue.getCity ());
    if (aValue.getCountry () != null)
      aElement.setAttributeWithConversion (ATTR_COUNTRY, aValue.getCountry ());
    if (StringHelper.hasText (aValue.getRegistrationName ()))
      aElement.setAttribute (ATTR_REGISTRATIONNAME, aValue.getRegistrationName ());
    if (StringHelper.hasText (aValue.getCompanyRegistrationNumber ()))
      aElement.setAttribute (ATTR_COMPANYREGISTRATIONNUMBER, aValue.getCompanyRegistrationNumber ());
    if (StringHelper.hasText (aValue.getVATIN ()))
      aElement.setAttribute (ATTR_VATIN, aValue.getVATIN ());
    if (StringHelper.hasText (aValue.getBIC ()))
      aElement.setAttribute (ATTR_BIC, aValue.getBIC ());
    if (StringHelper.hasText (aValue.getVATIN ()))
      aElement.setAttribute (ATTR_IBAN, aValue.getIBAN ());
  }

  @Nonnull
  protected final void convertToNativePartial (@Nonnull final AbstractProcParty aValue,
                                               @Nonnull final IMicroElement aElement)
  {
    aValue.setID (StringParser.parseInt (aElement.getAttribute (ATTR_ID), CGlobal.ILLEGAL_UINT));
    aValue.setDeleted (StringParser.parseBool (aElement.getAttribute (ATTR_DELETED),
                                               AbstractProcDeletableObject.DEFAULT_DELETED));
    final String sEntityIDScheme = aElement.getAttribute (ATTR_ENTITYIDSCHEMEID);
    aValue.setEntityIDScheme (EntityIDSchemeManager.getInstance ().getIDSchemeFromID (sEntityIDScheme));
    aValue.setEntityID (aElement.getAttribute (ATTR_ENTITYID));
    aValue.setEndpointID (aElement.getAttribute (ATTR_ENDPOINTID));
    aValue.setName (aElement.getAttribute (ATTR_NAME));
    aValue.setStreet (aElement.getAttribute (ATTR_STREET));
    aValue.setBuildingNumber (aElement.getAttribute (ATTR_BUILDINGNUMBER));
    aValue.setPostbox (aElement.getAttribute (ATTR_POSTBOX));
    aValue.setZipCode (aElement.getAttribute (ATTR_ZIPCODE));
    aValue.setCity (aElement.getAttribute (ATTR_CITY));
    aValue.setCountry (aElement.getAttributeWithConversion (ATTR_COUNTRY, Locale.class));
    aValue.setRegistrationName (aElement.getAttribute (ATTR_REGISTRATIONNAME));
    aValue.setCompanyRegistrationNumber (aElement.getAttribute (ATTR_COMPANYREGISTRATIONNUMBER));
    aValue.setVATIN (aElement.getAttribute (ATTR_VATIN));
    aValue.setBIC (aElement.getAttribute (ATTR_BIC));
    aValue.setIBAN (aElement.getAttribute (ATTR_IBAN));
  }
}
