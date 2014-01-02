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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.mime.CMimeType;
import com.phloc.datetime.PDTFactory;

/**
 * Test class for class {@link ProcResourceAttachment}.
 * 
 * @author Philip Helger
 */
public final class ProcResourceAttachmentTest
{
  @Test
  public void testAll ()
  {
    // Explicit title
    ProcResourceAttachment aPFSA = new ProcResourceAttachment ("any",
                                                               "my title",
                                                               CMimeType.TEXT_PLAIN,
                                                               PDTFactory.getCurrentDateTime (),
                                                               new ClassPathResource ("test.wiki"));
    assertEquals ("any", aPFSA.getID ());
    assertEquals ("my title", aPFSA.getTitle ());
    assertEquals (CMimeType.TEXT_PLAIN, aPFSA.getMIMEType ());
    assertEquals ("SGFsbG8gV2VsdCE=", aPFSA.getBase64Encoded ());

    // Use file name as title
    aPFSA = new ProcResourceAttachment ("any",
                                        CMimeType.TEXT_PLAIN,
                                        PDTFactory.getCurrentDateTime (),
                                        new ClassPathResource ("test.wiki"));
    assertTrue (aPFSA.getTitle ().endsWith ("test.wiki"));
    assertEquals (CMimeType.TEXT_PLAIN, aPFSA.getMIMEType ());
    assertEquals ("SGFsbG8gV2VsdCE=", aPFSA.getBase64Encoded ());
  }
}
