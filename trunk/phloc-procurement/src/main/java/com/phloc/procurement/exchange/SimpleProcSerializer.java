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
package com.phloc.procurement.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.joda.time.LocalDate;

import com.phloc.commons.CGlobal;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeType;
import com.phloc.commons.string.StringParser;
import com.phloc.masterdata.price.ReadonlyPrice;
import com.phloc.masterdata.tax.ETaxCategoryUN5305;
import com.phloc.masterdata.trade.EIncoterm;
import com.phloc.procurement.attachment.IProcAttachment;
import com.phloc.procurement.attachment.ProcAttachmentManager;
import com.phloc.procurement.attachment.ProcAttachmentManagerFactory;
import com.phloc.procurement.attachment.ProcInMemoryAttachment;
import com.phloc.procurement.codelist.ECommodityScheme;
import com.phloc.procurement.domain.AbstractProcDeletableObject;
import com.phloc.procurement.domain.AbstractProcObject;
import com.phloc.procurement.domain.IProcDeletableObject;
import com.phloc.procurement.domain.IProcObject;
import com.phloc.procurement.idscheme.EntityIDScheme;
import com.phloc.procurement.invoice.IProcInvoice;
import com.phloc.procurement.invoice.IProcInvoiceAttachment;
import com.phloc.procurement.invoice.IProcInvoiceLine;
import com.phloc.procurement.invoice.ProcInvoice;
import com.phloc.procurement.invoice.ProcInvoiceAttachment;
import com.phloc.procurement.invoice.ProcInvoiceLine;
import com.phloc.procurement.order.IProcOrder;
import com.phloc.procurement.order.IProcOrderAttachment;
import com.phloc.procurement.order.IProcOrderItem;
import com.phloc.procurement.order.ProcOrder;
import com.phloc.procurement.order.ProcOrderAttachment;
import com.phloc.procurement.order.ProcOrderItem;
import com.phloc.procurement.party.AbstractProcParty;
import com.phloc.procurement.party.IProcCustomer;
import com.phloc.procurement.party.IProcCustomerPaymentTerm;
import com.phloc.procurement.party.IProcMasterData;
import com.phloc.procurement.party.IProcParty;
import com.phloc.procurement.party.IProcSupplier;
import com.phloc.procurement.party.ProcCustomer;
import com.phloc.procurement.party.ProcCustomerPaymentTerm;
import com.phloc.procurement.party.ProcMasterData;
import com.phloc.procurement.party.ProcSupplier;

/**
 * Read and write procurement domain objects from and to XML - quick and dirty.
 * 
 * @author philip
 */
@Immutable
public final class SimpleProcSerializer
{
  public static final String ELEMENT_MASTERDATA = "masterdata";
  public static final String ELEMENT_CUSTOMER = "customer";
  public static final String ELEMENT_SUPPLIER = "supplier";
  public static final String ELEMENT_ORDER = "order";
  public static final String ELEMENT_INVOICE = "invoice";

  private SimpleProcSerializer ()
  {}

  private static void _writeObject (@Nonnull final IProcObject aObject, @Nonnull final IMicroElement eElement)
  {
    eElement.setAttribute ("id", aObject.getID ());
  }

  private static void _readObject (@Nonnull final AbstractProcObject aObject,
                                   @Nonnull final IMicroElement eElement,
                                   final boolean bReadID)
  {
    if (bReadID)
      aObject.setID (StringParser.parseInt (eElement.getAttribute ("id"), -1));
  }

  private static void _writeDeletableObject (@Nonnull final IProcDeletableObject aDeletableObj,
                                             @Nonnull final IMicroElement eElement)
  {
    _writeObject (aDeletableObj, eElement);
    eElement.setAttribute ("deleted", Boolean.toString (aDeletableObj.isDeleted ()));
  }

  private static void _readDeletableObject (@Nonnull final AbstractProcDeletableObject aDeletableObj,
                                            @Nonnull final IMicroElement eElement,
                                            final boolean bReadID)
  {
    _readObject (aDeletableObj, eElement, bReadID);
    aDeletableObj.setDeleted (StringParser.parseBool (eElement.getAttribute ("deleted")));
  }

  private static void _writeParty (@Nonnull final IProcParty aParty, @Nonnull final IMicroElement eElement)
  {
    _writeDeletableObject (aParty, eElement);
    final IMicroElement eID = eElement.appendElement ("entityid");
    if (aParty.getEntityIDScheme () != null)
      eID.setAttributeWithConversion ("scheme", aParty.getEntityIDScheme ());
    if (aParty.getEntityID () != null)
      eID.appendText (aParty.getEntityID ());
    if (aParty.getEndpointID () != null)
      eElement.appendElement ("endpointid").appendText (aParty.getEndpointID ());
    if (aParty.getName () != null)
      eElement.appendElement ("name").appendText (aParty.getName ());
    final IMicroElement eAddress = eElement.appendElement ("address");
    if (aParty.getStreet () != null)
      eAddress.appendElement ("street").appendText (aParty.getStreet ());
    if (aParty.getBuildingNumber () != null)
      eAddress.appendElement ("buildno").appendText (aParty.getBuildingNumber ());
    if (aParty.getPostbox () != null)
      eAddress.appendElement ("postbox").appendText (aParty.getPostbox ());
    if (aParty.getZipCode () != null)
      eAddress.appendElement ("zipcode").appendText (aParty.getZipCode ());
    if (aParty.getCity () != null)
      eAddress.appendElement ("city").appendText (aParty.getCity ());
    if (aParty.getCountry () != null)
      eAddress.appendElement ("country").appendTextWithConversion (aParty.getCountry ());
    if (aParty.getRegistrationName () != null)
      eElement.appendElement ("registrationname").appendText (aParty.getRegistrationName ());
    if (aParty.getCompanyRegistrationNumber () != null)
      eElement.appendElement ("companyregistrationnumber").appendText (aParty.getCompanyRegistrationNumber ());
    if (aParty.getVATIN () != null)
      eElement.appendElement ("vatin").appendText (aParty.getVATIN ());
    if (aParty.getBIC () != null)
      eElement.appendElement ("bic").appendText (aParty.getBIC ());
    if (aParty.getIBAN () != null)
      eElement.appendElement ("iban").appendText (aParty.getIBAN ());
  }

  private static void _readParty (@Nonnull final AbstractProcParty aParty,
                                  @Nonnull final IMicroElement eElement,
                                  final boolean bReadID)
  {
    _readDeletableObject (aParty, eElement, bReadID);
    final IMicroElement eID = eElement.getFirstChildElement ("entityid");
    aParty.setEntityIDScheme (eID.getAttributeWithConversion ("scheme", EntityIDScheme.class));
    aParty.setEntityID (eID.getTextContent ());
    aParty.setEndpointID (MicroUtils.getChildTextContent (eElement, "endpointid"));
    aParty.setName (MicroUtils.getChildTextContent (eElement, "name"));
    final IMicroElement eAddress = eElement.getFirstChildElement ("address");
    aParty.setStreet (MicroUtils.getChildTextContent (eAddress, "street"));
    aParty.setBuildingNumber (MicroUtils.getChildTextContent (eAddress, "buildno"));
    aParty.setPostbox (MicroUtils.getChildTextContent (eAddress, "postbox"));
    aParty.setZipCode (MicroUtils.getChildTextContent (eAddress, "zipcode"));
    aParty.setCity (MicroUtils.getChildTextContent (eAddress, "city"));
    aParty.setCountry (MicroUtils.getChildTextContentWithConversion (eAddress, "country", Locale.class));
    aParty.setRegistrationName (MicroUtils.getChildTextContent (eElement, "registrationname"));
    aParty.setCompanyRegistrationNumber (MicroUtils.getChildTextContent (eElement, "companyregistrationnumber"));
    aParty.setVATIN (MicroUtils.getChildTextContent (eElement, "vatin"));
    aParty.setBIC (MicroUtils.getChildTextContent (eElement, "bic"));
    aParty.setIBAN (MicroUtils.getChildTextContent (eElement, "iban"));
  }

  private static void _writeAttachment (@Nonnull final IProcAttachment aAttachment,
                                        @Nonnull final IMicroElement aElement)
  {
    final IMicroElement eAttachment = aElement.appendElement ("attachment");
    eAttachment.setAttribute ("title", aAttachment.getTitle ());
    if (aAttachment.getMIMEType () != null)
      eAttachment.setAttribute ("mimetype", aAttachment.getMIMEType ().getAsString ());
    eAttachment.appendText (aAttachment.getBase64Encoded ());
  }

  @Nonnull
  private static IProcAttachment _readAttachment (@Nonnull final IMicroElement eAttachment)
  {
    final String sTitle = eAttachment.getAttribute ("title");
    final IMimeType aMIMEType = MimeType.parseFromStringWithoutEncoding (eAttachment.getAttribute ("mimetype"));
    final byte [] aData = Base64Helper.safeDecode (eAttachment.getTextContent ());
    return new ProcInMemoryAttachment (GlobalIDFactory.getNewPersistentStringID (), sTitle, aMIMEType, aData);
  }

  @Nonnull
  public static IMicroElement writeMasterData (@Nonnull final IProcMasterData aMasterData)
  {
    final IMicroElement eElement = new MicroElement (ELEMENT_MASTERDATA);
    _writeParty (aMasterData, eElement);
    eElement.appendElement ("client").appendText (Integer.toString (aMasterData.getClient ()));
    if (aMasterData.getEmail () != null)
      eElement.appendElement ("email").appendText (aMasterData.getEmail ());
    if (aMasterData.getTelephone () != null)
      eElement.appendElement ("telephone").appendText (aMasterData.getTelephone ());
    if (aMasterData.getFirstName () != null)
      eElement.appendElement ("firstname").appendText (aMasterData.getFirstName ());
    if (aMasterData.getLastName () != null)
      eElement.appendElement ("lastname").appendText (aMasterData.getLastName ());
    return eElement;
  }

  @Nonnull
  public static ProcMasterData readMasterData (@Nonnull final IMicroElement eElement, final boolean bReadID)
  {
    final ProcMasterData aMasterData = new ProcMasterData ();
    _readParty (aMasterData, eElement, bReadID);
    aMasterData.setClient (StringParser.parseInt (MicroUtils.getChildTextContent (eElement, "client"),
                                                  CGlobal.ILLEGAL_UINT));
    aMasterData.setEmail (MicroUtils.getChildTextContent (eElement, "email"));
    aMasterData.setTelephone (MicroUtils.getChildTextContent (eElement, "telephone"));
    aMasterData.setFirstName (MicroUtils.getChildTextContent (eElement, "firstname"));
    aMasterData.setLastName (MicroUtils.getChildTextContent (eElement, "lastname"));
    return aMasterData;
  }

  @Nonnull
  public static IMicroElement writeCustomer (@Nonnull final IProcCustomer aCustomer)
  {
    final IMicroElement eElement = new MicroElement (ELEMENT_CUSTOMER);
    _writeParty (aCustomer, eElement);
    eElement.appendElement ("client").appendText (Integer.toString (aCustomer.getClient ()));
    final List <? extends IProcCustomerPaymentTerm> aPaymentTerms = aCustomer.getPaymentTerms ();
    if (aPaymentTerms != null)
      for (final IProcCustomerPaymentTerm aPaymentTerm : aPaymentTerms)
      {
        final IMicroElement ePaymentTerm = eElement.appendElement ("paymentterm");
        _writeObject (aPaymentTerm, ePaymentTerm);
        ePaymentTerm.setAttribute ("percentage", Double.toString (aPaymentTerm.getPercentage ()));
        ePaymentTerm.setAttribute ("days", aPaymentTerm.getDays ());
        if (aPaymentTerm.getDescription () != null)
          ePaymentTerm.appendElement ("description").appendText (aPaymentTerm.getDescription ());
      }
    return eElement;
  }

  @Nonnull
  public static ProcCustomer readCustomer (@Nonnull final IMicroElement eElement, final boolean bReadID)
  {
    final ProcCustomer aCustomer = new ProcCustomer ();
    _readParty (aCustomer, eElement, bReadID);
    aCustomer.setClient (StringParser.parseInt (MicroUtils.getChildTextContent (eElement, "client"),
                                                CGlobal.ILLEGAL_UINT));
    final List <ProcCustomerPaymentTerm> aPaymentTerms = new ArrayList <ProcCustomerPaymentTerm> ();
    for (final IMicroElement ePaymentTerm : eElement.getAllChildElements ("paymentterm"))
    {
      final ProcCustomerPaymentTerm aPaymentTerm = new ProcCustomerPaymentTerm (aCustomer);
      _readObject (aPaymentTerm, ePaymentTerm, bReadID);
      aPaymentTerm.setPercentage (StringParser.parseDouble (ePaymentTerm.getAttribute ("percentage"), Double.NaN));
      aPaymentTerm.setDays (StringParser.parseInt (ePaymentTerm.getAttribute ("days"), -1));
      aPaymentTerm.setDescription (MicroUtils.getChildTextContent (ePaymentTerm, "description"));
      aPaymentTerms.add (aPaymentTerm);
    }
    aCustomer.setPaymentTerms (aPaymentTerms);
    return aCustomer;
  }

  @Nonnull
  public static IMicroElement writeSupplier (@Nonnull final IProcSupplier aSupplier)
  {
    final IMicroElement eElement = new MicroElement (ELEMENT_SUPPLIER);
    _writeParty (aSupplier, eElement);
    eElement.appendElement ("client").appendText (Integer.toString (aSupplier.getClient ()));
    return eElement;
  }

  @Nonnull
  public static ProcSupplier readSupplier (@Nonnull final IMicroElement eElement, final boolean bReadID)
  {
    final ProcSupplier aSupplier = new ProcSupplier ();
    _readParty (aSupplier, eElement, bReadID);
    aSupplier.setClient (StringParser.parseInt (MicroUtils.getChildTextContent (eElement, "client"),
                                                CGlobal.ILLEGAL_UINT));
    return aSupplier;
  }

  @Nonnull
  public static IMicroElement writeOrder (@Nonnull final IProcOrder aOrder)
  {
    final IMicroElement eElement = new MicroElement (ELEMENT_ORDER);
    _writeDeletableObject (aOrder, eElement);
    if (aOrder.getOrderNumber () != null)
      eElement.appendElement ("ordernumber").appendText (aOrder.getOrderNumber ());
    if (aOrder.getIssueDate () != null)
      eElement.appendElement ("issuedate").appendTextWithConversion (aOrder.getIssueDate ());
    if (aOrder.getDeliveryStartDate () != null)
      eElement.appendElement ("deliverystartdate").appendTextWithConversion (aOrder.getDeliveryStartDate ());
    if (aOrder.getDeliveryEndDate () != null)
      eElement.appendElement ("deliveryenddate").appendTextWithConversion (aOrder.getDeliveryEndDate ());
    if (aOrder.getIncoterm () != null)
      eElement.appendElement ("incoterm").appendTextWithConversion (aOrder.getIncoterm ());
    final List <? extends IProcOrderItem> aOrderItems = aOrder.getOrderItems ();
    if (aOrderItems != null)
      for (final IProcOrderItem aOrderItem : aOrderItems)
      {
        final IMicroElement eOrderItem = eElement.appendElement ("orderitem");
        _writeObject (aOrderItem, eOrderItem);
        if (aOrderItem.getDescription () != null)
          eOrderItem.appendElement ("description").appendText (aOrderItem.getDescription ());
        if (aOrderItem.getNote () != null)
          eOrderItem.appendElement ("note").appendText (aOrderItem.getNote ());
        eOrderItem.appendElement ("quantity").appendText (Integer.toString (aOrderItem.getQuantity ()));
        if (aOrderItem.getUnitPrice () != null)
          eOrderItem.appendChild (MicroTypeConverter.convertToMicroElement (aOrderItem.getUnitPrice (), "unitprice"));
        if (aOrderItem.getCommodityScheme () != null)
          eOrderItem.appendElement ("commodityscheme").appendTextWithConversion (aOrderItem.getCommodityScheme ());
        if (aOrderItem.getCommodityID () != null)
          eOrderItem.appendElement ("commodityid").appendText (aOrderItem.getCommodityID ());
      }
    final List <? extends IProcOrderAttachment> aAttachments = aOrder.getAttachments ();
    if (aAttachments != null)
    {
      // Get the attachment manager for resolving
      final ProcAttachmentManager aAttachmentMgr = ProcAttachmentManagerFactory.getAttachmentManager ();
      for (final IProcOrderAttachment aOrderAttachment : aAttachments)
      {
        final String sAttachmentID = aOrderAttachment.getAttachmentID ();
        final IProcAttachment aAttachment = aAttachmentMgr.getAttachmentOfID (sAttachmentID);
        if (aAttachment != null)
          _writeAttachment (aAttachment, eElement);
      }
    }
    return eElement;
  }

  @Nonnull
  public static ProcOrder readOrder (@Nonnull final IMicroElement eElement,
                                     @Nonnull final IProcAttachmentReadHandler aHandler,
                                     final boolean bReadID)
  {
    final ProcOrder aOrder = new ProcOrder ();
    _readDeletableObject (aOrder, eElement, bReadID);
    aOrder.setOrderNumber (MicroUtils.getChildTextContent (eElement, "ordernumber"));
    aOrder.setIssueDate (MicroUtils.getChildTextContentWithConversion (eElement, "issuedate", LocalDate.class));
    aOrder.setDeliveryStartDate (MicroUtils.getChildTextContentWithConversion (eElement,
                                                                               "deliverystartdate",
                                                                               LocalDate.class));
    aOrder.setDeliveryEndDate (MicroUtils.getChildTextContentWithConversion (eElement,
                                                                             "deliveryenddate",
                                                                             LocalDate.class));
    aOrder.setIncoterm (MicroUtils.getChildTextContentWithConversion (eElement, "incoterm", EIncoterm.class));

    // Read all order items
    final List <ProcOrderItem> aOrderItems = new ArrayList <ProcOrderItem> ();
    for (final IMicroElement eOrderItem : eElement.getAllChildElements ("orderitem"))
    {
      final ProcOrderItem aOrderItem = new ProcOrderItem (aOrder);
      _readObject (aOrderItem, eOrderItem, bReadID);
      aOrderItem.setDescription (MicroUtils.getChildTextContent (eOrderItem, "description"));
      aOrderItem.setNote (MicroUtils.getChildTextContent (eOrderItem, "note"));
      aOrderItem.setQuantity (StringParser.parseInt (MicroUtils.getChildTextContent (eOrderItem, "quantity"), -1));
      aOrderItem.setUnitPrice (MicroTypeConverter.convertToNative (eOrderItem.getFirstChildElement ("unitprice"),
                                                                   ReadonlyPrice.class));
      aOrderItem.setCommodityScheme (MicroUtils.getChildTextContentWithConversion (eOrderItem,
                                                                                   "commodityscheme",
                                                                                   ECommodityScheme.class));
      aOrderItem.setCommodityID (MicroUtils.getChildTextContent (eOrderItem, "commodityid"));
      aOrderItems.add (aOrderItem);
    }
    aOrder.setOrderItems (aOrderItems);

    // Read all attachments
    final List <ProcOrderAttachment> aAttachments = new ArrayList <ProcOrderAttachment> ();
    for (final IMicroElement eAttachment : eElement.getAllChildElements ("attachment"))
    {
      final IProcAttachment aAttachment = aHandler.handleReadAttachment (_readAttachment (eAttachment));
      aAttachments.add (new ProcOrderAttachment (aOrder, aAttachment));
    }
    aOrder.setAttachments (aAttachments);
    return aOrder;
  }

  @Nonnull
  public static IMicroElement writeInvoice (@Nonnull final IProcInvoice aInvoice)
  {
    final IMicroElement eElement = new MicroElement (ELEMENT_INVOICE);
    _writeDeletableObject (aInvoice, eElement);
    if (aInvoice.getIssueDate () != null)
      eElement.appendElement ("issuedate").appendTextWithConversion (aInvoice.getIssueDate ());
    if (aInvoice.getInvoiceNumber () != null)
      eElement.appendElement ("invoicenumber").appendText (aInvoice.getInvoiceNumber ());
    if (aInvoice.getInvoiceNote () != null)
      eElement.appendElement ("invoicenote").appendText (aInvoice.getInvoiceNote ());
    final List <? extends IProcInvoiceLine> aInvoiceLines = aInvoice.getInvoiceLines ();
    if (aInvoiceLines != null)
      for (final IProcInvoiceLine aInvoiceLine : aInvoiceLines)
      {
        final IMicroElement eInvoiceLine = eElement.appendElement ("invoiceline");
        _writeObject (aInvoiceLine, eInvoiceLine);
        if (aInvoiceLine.getOrderItemID () != null)
          eInvoiceLine.appendElement ("orderitemid").appendText (aInvoiceLine.getOrderItemID ());
        if (aInvoiceLine.getLineID () != null)
          eInvoiceLine.appendElement ("lineid").appendText (aInvoiceLine.getLineID ());
        if (aInvoiceLine.getCommodityScheme () != null)
          eInvoiceLine.appendElement ("commodityscheme").appendTextWithConversion (aInvoiceLine.getCommodityScheme ());
        if (aInvoiceLine.getCommodityID () != null)
          eInvoiceLine.appendElement ("commodityid").appendText (aInvoiceLine.getCommodityID ());
        if (aInvoiceLine.getDescription () != null)
          eInvoiceLine.appendElement ("description").appendText (aInvoiceLine.getDescription ());
        eInvoiceLine.appendElement ("quantity").appendText (Integer.toString (aInvoiceLine.getQuantity ()));
        if (aInvoiceLine.getUnitPrice () != null)
          eInvoiceLine.appendChild (MicroTypeConverter.convertToMicroElement (aInvoiceLine.getUnitPrice (), "unitprice"));
        if (aInvoiceLine.getTaxCategory () != null)
          eInvoiceLine.appendElement ("taxcategory").appendTextWithConversion (aInvoiceLine.getTaxCategory ());
        if (aInvoiceLine.getRemark () != null)
          eInvoiceLine.appendElement ("remark").appendText (aInvoiceLine.getRemark ());
      }
    final List <? extends IProcInvoiceAttachment> aAttachments = aInvoice.getAttachments ();
    if (aAttachments != null)
    {
      // Get the attachment manager for resolving
      final ProcAttachmentManager aAttachmentMgr = ProcAttachmentManagerFactory.getAttachmentManager ();
      for (final IProcInvoiceAttachment aInvoiceAttachment : aAttachments)
      {
        final String sAttachmentID = aInvoiceAttachment.getAttachmentID ();
        final IProcAttachment aAttachment = aAttachmentMgr.getAttachmentOfID (sAttachmentID);
        if (aAttachment != null)
          _writeAttachment (aAttachment, eElement);
      }
    }
    return eElement;
  }

  @Nonnull
  public static ProcInvoice readInvoice (@Nonnull final IMicroElement eElement,
                                         @Nonnull final IProcAttachmentReadHandler aHandler,
                                         final boolean bReadID)
  {
    final ProcInvoice aInvoice = new ProcInvoice ();
    _readDeletableObject (aInvoice, eElement, bReadID);
    aInvoice.setIssueDate (MicroUtils.getChildTextContentWithConversion (eElement, "issuedate", LocalDate.class));
    aInvoice.setInvoiceNumber (MicroUtils.getChildTextContent (eElement, "invoicenumber"));
    aInvoice.setInvoiceNote (MicroUtils.getChildTextContent (eElement, "invoicenote"));

    // Read all invoice lines
    final List <ProcInvoiceLine> aInvoiceLines = new ArrayList <ProcInvoiceLine> ();
    for (final IMicroElement eInvoiceLine : eElement.getAllChildElements ("invoiceline"))
    {
      final ProcInvoiceLine aInvoiceLine = new ProcInvoiceLine (aInvoice);
      _readObject (aInvoiceLine, eInvoiceLine, bReadID);
      aInvoiceLine.setOrderItemID (MicroUtils.getChildTextContent (eInvoiceLine, "orderitemid"));
      aInvoiceLine.setLineID (MicroUtils.getChildTextContent (eInvoiceLine, "lineid"));
      aInvoiceLine.setCommodityScheme (MicroUtils.getChildTextContentWithConversion (eInvoiceLine,
                                                                                     "commodityscheme",
                                                                                     ECommodityScheme.class));
      aInvoiceLine.setCommodityID (MicroUtils.getChildTextContent (eInvoiceLine, "commodityid"));
      aInvoiceLine.setDescription (MicroUtils.getChildTextContent (eInvoiceLine, "description"));
      aInvoiceLine.setQuantity (StringParser.parseInt (MicroUtils.getChildTextContent (eInvoiceLine, "quantity"), -1));
      aInvoiceLine.setUnitPrice (MicroTypeConverter.convertToNative (eInvoiceLine.getFirstChildElement ("unitprice"),
                                                                     ReadonlyPrice.class));
      aInvoiceLine.setTaxCategory (MicroUtils.getChildTextContentWithConversion (eInvoiceLine,
                                                                                 "taxcategory",
                                                                                 ETaxCategoryUN5305.class));
      aInvoiceLine.setRemark (MicroUtils.getChildTextContent (eInvoiceLine, "remark"));
      aInvoiceLines.add (aInvoiceLine);
    }
    aInvoice.setInvoiceLines (aInvoiceLines);

    // Read all attachments
    final List <ProcInvoiceAttachment> aAttachments = new ArrayList <ProcInvoiceAttachment> ();
    for (final IMicroElement eAttachment : eElement.getAllChildElements ("attachment"))
    {
      // Read the attachment and persist it
      final IProcAttachment aAttachment = aHandler.handleReadAttachment (_readAttachment (eAttachment));

      // Link the attachment to the invoice
      aAttachments.add (new ProcInvoiceAttachment (aInvoice, aAttachment));
    }
    aInvoice.setAttachments (aAttachments);
    return aInvoice;
  }
}
