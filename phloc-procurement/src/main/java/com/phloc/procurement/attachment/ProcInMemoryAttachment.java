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
package com.phloc.procurement.attachment;

import java.io.InputStream;
import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.base64.Base64;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.PDTFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * In-memory implementation of the {@link IProcAttachment} interface. The data
 * is stored in a byte array.
 * 
 * @author Philip Helger
 */
public final class ProcInMemoryAttachment extends AbstractProcAttachment
{
  private final byte [] m_aData;

  @SuppressFBWarnings ("EI_EXPOSE_REP2")
  public ProcInMemoryAttachment (@Nonnull @Nonempty final String sID,
                                 @Nonnull @Nonempty final String sTitle,
                                 @Nullable final IMimeType aMIMEType,
                                 @Nonnull final byte [] aData)
  {
    super (sID, sTitle, aMIMEType, PDTFactory.getCurrentDateTime ());
    if (aData == null)
      throw new NullPointerException ("data");
    m_aData = aData;
  }

  @Nonnull
  public InputStream getInputStream ()
  {
    return new NonBlockingByteArrayInputStream (m_aData);
  }

  public boolean isPersisted ()
  {
    return false;
  }

  @Nonnegative
  public int getSize ()
  {
    return m_aData.length;
  }

  @Nonnull
  public String getBase64Encoded ()
  {
    return Base64.encodeBytes (m_aData);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ProcInMemoryAttachment rhs = (ProcInMemoryAttachment) o;
    return Arrays.equals (m_aData, rhs.m_aData);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aData).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("length", m_aData.length).toString ();
  }
}
