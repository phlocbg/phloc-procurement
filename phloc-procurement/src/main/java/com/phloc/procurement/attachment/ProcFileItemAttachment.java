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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.fileupload.FileItemResource;
import com.phloc.web.fileupload.IFileItem;

/**
 * A special attachment for upload of files. It is based on a {@link IFileItem}
 * 
 * @author Philip Helger
 */
public class ProcFileItemAttachment extends ProcResourceAttachment
{
  public ProcFileItemAttachment (@Nonnull @Nonempty final String sID, @Nonnull final IFileItem aFileItem)
  {
    super (sID, aFileItem.getName (), PDTFactory.getCurrentDateTime (), new FileItemResource (aFileItem));
  }

  @Override
  public boolean isPersisted ()
  {
    return false;
  }
}
