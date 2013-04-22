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

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Handles the storage of procurement attachments.
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public interface IProcAttachmentStorageHandlerSPI
{
  /**
   * @return A set of all known attachment IDs.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <String> getAllAttachmentIDs ();

  /**
   * Check if the passed attachment is contained.
   * 
   * @param sAttachmentID
   *        The ID of the attachment to search. May be <code>null</code>.
   * @return <code>true</code> if the attachment is contained,
   *         <code>false</code> otherwise.
   */
  boolean containsAttachmentOfID (@Nullable String sAttachmentID);

  /**
   * Get the attachment with the given ID.
   * 
   * @param sAttachmentID
   *        The ID to be resolved. May be <code>null</code>.
   * @return <code>null</code> if no such attachment exists.
   */
  @Nullable
  IProcAttachment getAttachmentOfID (@Nullable String sAttachmentID);

  /**
   * Persist the attachment, and return a reference to the persisted attachment.
   * The ID of the passed attachment is used for persistence.
   * 
   * @param aAttachment
   *        The attachment to be persisted. May not be <code>null</code>.
   * @return The reference to the persisted attachment. If nothing is changed,
   *         the passed attachment from the parameter should be returned. Never
   *         <code>null</code>.
   */
  @Nonnull
  IProcAttachment persistAttachment (@Nonnull IProcAttachment aAttachment);

  /**
   * Remove the attachment with the passed ID.
   * 
   * @param sAttachmentID
   *        The ID of the attachment to be removed. May be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  EChange removeAttachment (@Nullable String sAttachmentID);
}
