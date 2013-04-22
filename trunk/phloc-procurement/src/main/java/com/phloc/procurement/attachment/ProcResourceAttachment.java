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
package com.phloc.procurement.attachment;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.base64.Base64;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class references a single attachment that can be found on disk.
 * 
 * @author Philip Helger
 */
public class ProcResourceAttachment extends AbstractProcAttachment
{
  private final IReadableResource m_aRes;

  public ProcResourceAttachment (@Nonnull @Nonempty final String sID,
                                 @Nonnull final DateTime aUploadDT,
                                 @Nonnull final IReadableResource aRes)
  {
    this (sID, aRes.getResourceID (), aUploadDT, aRes);
  }

  public ProcResourceAttachment (@Nonnull @Nonempty final String sID,
                                 @Nullable final IMimeType aMIMEType,
                                 @Nonnull final DateTime aUploadDT,
                                 @Nonnull final IReadableResource aRes)
  {
    this (sID, aRes.getResourceID (), aMIMEType, aUploadDT, aRes);
  }

  public ProcResourceAttachment (@Nonnull @Nonempty final String sID,
                                 @Nonnull @Nonempty final String sTitle,
                                 @Nonnull final DateTime aUploadDT,
                                 @Nonnull final IReadableResource aRes)
  {
    this (sID, sTitle, null, aUploadDT, aRes);
  }

  public ProcResourceAttachment (@Nonnull @Nonempty final String sID,
                                 @Nonnull @Nonempty final String sTitle,
                                 @Nullable final IMimeType aMIMEType,
                                 @Nonnull final DateTime aUploadDT,
                                 @Nonnull final IReadableResource aRes)
  {
    super (sID,
           sTitle,
           aMIMEType != null ? aMIMEType : MimeTypeDeterminator.getMimeTypeObjectFromFilename (aRes.getPath ()),
           aUploadDT);
    m_aRes = aRes;
  }

  @Nullable
  public InputStream getInputStream ()
  {
    return m_aRes.getInputStream ();
  }

  public boolean isPersisted ()
  {
    return true;
  }

  @Nonnull
  public String getBase64Encoded ()
  {
    final Base64.InputStream aIS = new Base64.InputStream (getInputStream (), Base64.ENCODE);
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    StreamUtils.copyInputStreamToOutputStreamAndCloseOS (aIS, aBAOS);
    return aBAOS.getAsString (CCharset.CHARSET_ISO_8859_1_OBJ);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcResourceAttachment rhs = (ProcResourceAttachment) o;
    return m_aRes.equals (rhs.m_aRes);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aRes).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("res", m_aRes).toString ();
  }
}
