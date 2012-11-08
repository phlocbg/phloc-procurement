/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.procurement.exchange.ubl;

import com.phloc.commons.string.StringHelper;

public enum EUBLInvoiceCustomization implements IUBLCACCustomization
{
  STANDARD,
  PEPPOL
  {
    @Override
    public String getCustomizationSchemeID ()
    {
      return "PEPPOL";
    }

    @Override
    public String getCustomizationValue ()
    {
      return "urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0";
    }

    @Override
    public String getProfileID ()
    {
      return "urn:www.cenbii.eu:profile:bii04:ver1.0";
    }

    @Override
    public boolean isCountryNameAllowed ()
    {
      return false;
    }

    @Override
    public boolean isPartyContactNameAllowed ()
    {
      return false;
    }

    @Override
    public boolean isInvoiceLineTaxSubtotalAllowed ()
    {
      return false;
    }

    @Override
    public boolean isInvoiceLineCommodityClassificationAllowed ()
    {
      return false;
    }

    @Override
    public String ensureNameLength (final String sName)
    {
      if (StringHelper.getLength (sName) > 50)
        return sName.substring (0, 48) + "...";
      return sName;
    }
  };

  public String getCustomizationSchemeID ()
  {
    return null;
  }

  public String getCustomizationValue ()
  {
    return "phloc-procurement";
  }

  public String getProfileID ()
  {
    return null;
  }

  public boolean isCountryNameAllowed ()
  {
    return true;
  }

  public boolean isPartyContactNameAllowed ()
  {
    return true;
  }

  public boolean isInvoiceLineTaxSubtotalAllowed ()
  {
    return true;
  }

  public boolean isInvoiceLineCommodityClassificationAllowed ()
  {
    return true;
  }

  public String ensureNameLength (final String sName)
  {
    return sName;
  }
}
