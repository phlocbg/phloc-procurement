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

import javax.annotation.Nonnull;

import com.phloc.procurement.attachment.IProcAttachment;

/**
 * Defines how to handle serialized attachments.
 * 
 * @author Philip Helger
 */
public interface IProcAttachmentReadHandler
{
  /**
   * Handle the passed read attachment.
   * 
   * @param aAttachment
   *        The non-persisted attachment to be handled.
   * @return The handled attachment. May be the same as the read attachment but
   *         does not need to be. E.g. when storing the attachment somewhere,
   *         the result is the link to the persisted attachment.
   */
  @Nonnull
  IProcAttachment handleReadAttachment (@Nonnull IProcAttachment aAttachment);
}
