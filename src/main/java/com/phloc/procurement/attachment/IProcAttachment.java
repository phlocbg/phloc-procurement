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
package com.phloc.procurement.attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.mime.IMimeType;

/**
 * Describes a single attachment to a procurement document.
 * 
 * @author philip
 */
public interface IProcAttachment extends IInputStreamProvider, IHasID <String>
{
  /**
   * @return The title of this attachment. Can e.g. be the file name.
   */
  @Nonnull
  @Nonempty
  String getTitle ();

  /**
   * @return The MIME type of this document.
   */
  @Nullable
  IMimeType getMIMEType ();

  /**
   * @return <code>true</code> if the attachment is already persisted on some
   *         storage
   */
  boolean isPersisted ();

  /**
   * @return The base64 encoded version of this attachment's data.
   */
  @Nonnull
  String getBase64Encoded ();
}
