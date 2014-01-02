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

import java.util.ServiceLoader;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.exceptions.InitializationException;

/**
 * Use this class to retrieve the attachment manager. It uses the storage
 * handler loaded by SPI.
 * 
 * @author Philip Helger
 */
@Immutable
public final class ProcAttachmentManagerFactory
{
  private static final ProcAttachmentManager s_aInstance;

  static
  {
    // Find the SPI implementation
    IProcAttachmentStorageHandlerSPI aStorageHandler = null;
    for (final IProcAttachmentStorageHandlerSPI aSPI : ServiceLoader.load (IProcAttachmentStorageHandlerSPI.class))
      if (aSPI != null)
      {
        aStorageHandler = aSPI;
        break;
      }
    if (aStorageHandler == null)
      throw new InitializationException ("No storage handler for attachments found!");

    // And instantiate
    s_aInstance = new ProcAttachmentManager (aStorageHandler);
  }

  @Nonnull
  public static ProcAttachmentManager getAttachmentManager ()
  {
    return s_aInstance;
  }
}
