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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract implementation of the {@link IProcAttachment} interface that is
 * independent of the underlying data storage.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractProcAttachment implements IProcAttachment
{
  private final String m_sID;
  private final String m_sTitle;
  private final IMimeType m_aMIMEType;
  private final DateTime m_aUploadDT;

  public AbstractProcAttachment (@Nonnull @Nonempty final String sID,
                                 @Nonnull @Nonempty final String sTitle,
                                 @Nullable final IMimeType aMIMEType,
                                 @Nonnull final DateTime aUploadDT)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    if (StringHelper.hasNoText (sTitle))
      throw new IllegalArgumentException ("title");
    if (aUploadDT == null)
      throw new NullPointerException ("uploadDT");
    m_sID = sID;
    m_sTitle = sTitle;
    m_aMIMEType = aMIMEType;
    m_aUploadDT = aUploadDT;
  }

  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public final String getTitle ()
  {
    return m_sTitle;
  }

  @Nullable
  public final IMimeType getMIMEType ()
  {
    return m_aMIMEType;
  }

  @Nonnull
  public final DateTime getUploadDateTime ()
  {
    return m_aUploadDT;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractProcAttachment rhs = (AbstractProcAttachment) o;
    return m_sID.equals (rhs.m_sID) &&
           m_sTitle.equals (rhs.m_sTitle) &&
           EqualsUtils.equals (m_aMIMEType, rhs.m_aMIMEType) &&
           m_aUploadDT.equals (rhs.m_aUploadDT);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID)
                                       .append (m_sTitle)
                                       .append (m_aMIMEType)
                                       .append (m_aUploadDT)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("title", m_sTitle)
                                       .appendIfNotNull ("MIMEType", m_aMIMEType)
                                       .append ("uploadDT", m_aUploadDT)
                                       .toString ();
  }
}
