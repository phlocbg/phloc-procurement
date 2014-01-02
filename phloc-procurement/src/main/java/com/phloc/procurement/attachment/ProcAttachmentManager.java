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

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * This class handles attachments references e.g. by orders and invoices.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class ProcAttachmentManager implements IProcAttachmentResolver
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final IProcAttachmentStorageHandlerSPI m_aStorageHandler;

  public ProcAttachmentManager (@Nonnull final IProcAttachmentStorageHandlerSPI aStorageHandler)
  {
    if (aStorageHandler == null)
      throw new NullPointerException ("storageHandler");
    m_aStorageHandler = aStorageHandler;
  }

  @Nullable
  public IProcAttachment getAttachmentOfID (@Nullable final String sAttachmentID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aStorageHandler.getAttachmentOfID (sAttachmentID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Create a new attachment and store it with the contained storage handler.
   * 
   * @param aAttachment
   *        The attachment to be created.
   * @return The persisted attachment and never <code>null</code>. The persisted
   *         attachment may be different from the one passed in!
   */
  @Nonnull
  public IProcAttachment createAttachment (@Nonnull final IProcAttachment aAttachment)
  {
    if (aAttachment == null)
      throw new NullPointerException ("attachment");
    final String sAttachmentID = aAttachment.getID ();

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Is the ID already contained?
      if (m_aStorageHandler.containsAttachmentOfID (sAttachmentID))
        throw new IllegalArgumentException ("Passed attachment ID '" + sAttachmentID + "' is already contained!");

      // Persist
      final IProcAttachment aRealAttachment = m_aStorageHandler.persistAttachment (aAttachment);
      return aRealAttachment;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Remove the attachment with the passed ID.
   * 
   * @param sAttachmentID
   *        The attachment ID to be removed. May be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  public EChange removeAttachment (@Nullable final String sAttachmentID)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return m_aStorageHandler.removeAttachment (sAttachmentID);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return A non-<code>null</code> set of all known attachments.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllAttachmentIDs ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aStorageHandler.getAllAttachmentIDs ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
