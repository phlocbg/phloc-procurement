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
package com.phloc.procurement.domain;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasDisplayText;

/**
 * Contains all the different states as an enum! Never delete an item, as the
 * index of an item is stored in the DB!
 * 
 * @author Philip Helger
 */
public enum EProcState implements IHasID <String>, IHasDisplayText
{
  // ID is needed for EnumHelper!
  _UNDEFINED ("undefined", null, false),

  /* Created: manually created by me - no action */
  CREATED ("created", EProcStateName.CREATED, true),
  /* Sent to a partner */
  SENT ("sent", EProcStateName.SENT, false),
  /* Got acceptance from partner */
  ACCEPTED ("accepted", EProcStateName.ACCEPTED, false),
  /* Got rejection from partner */
  REJECTED ("rejected", EProcStateName.REJECTED, true),
  /* Incoming document I received */
  RECEIVED ("received", EProcStateName.RECEIVED, false),
  /* Invoice I sent out has been paid */
  PAID ("paid", EProcStateName.PAID, false);

  private final String m_sID;
  private final IHasDisplayText m_aDislayName;
  private final boolean m_bIsEditAllowed;

  private EProcState (@Nonnull @Nonempty final String sID,
                      @Nullable final IHasDisplayText aDisplayName,
                      final boolean bIsEditAllowed)
  {
    m_sID = sID;
    m_aDislayName = aDisplayName;
    m_bIsEditAllowed = bIsEditAllowed;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    if (m_aDislayName == null)
      throw new IllegalStateException ("This state has no text!");
    return m_aDislayName.getDisplayText (aContentLocale);
  }

  public boolean isEditAllowed ()
  {
    return m_bIsEditAllowed;
  }

  @Nullable
  public static EProcState getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EProcState.class, sID);
  }
}
