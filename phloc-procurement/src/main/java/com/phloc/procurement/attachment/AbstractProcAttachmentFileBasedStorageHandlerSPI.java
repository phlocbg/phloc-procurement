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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.reader.XMLListHandler;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeTypeParser;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.datetime.PDTFactory;

/**
 * Abstract class for a file-based attachment storage handler.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractProcAttachmentFileBasedStorageHandlerSPI implements IProcAttachmentStorageHandlerSPI
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractProcAttachmentFileBasedStorageHandlerSPI.class);
  private static final String FILENAME_ATTACHMENT_DAT = "attachment.dat";
  private static final String FILENAME_METADATA_XML = "metadata.xml";
  private static final String ATTR_ID = "id";
  private static final String ATTR_UPLOADDT = "uploaddt";
  private static final String ELEMENT_TITLE = "title";
  private static final String ELEMENT_MIMETYPE = "mimetype";

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  /** The base directory */
  private final File m_aBaseDir;
  /** The table of content files */
  private final File m_aTOCFile;
  /**
   * The set with all attachment IDs. Independent of loaded state. Sorted for
   * consistent storage.
   */
  private final Set <String> m_aAttachments = new TreeSet <String> ();
  /** The cache with the already resolved attachments from disk. */
  private final Map <String, IProcAttachment> m_aCache = new HashMap <String, IProcAttachment> ();

  /**
   * Constructor.
   * 
   * @param aBaseDir
   *        The base directory where the attachments should be handled.
   */
  public AbstractProcAttachmentFileBasedStorageHandlerSPI (@Nonnull final File aBaseDir)
  {
    if (aBaseDir == null)
      throw new NullPointerException ("baseDir");
    if (!aBaseDir.exists () || !aBaseDir.isDirectory ())
      throw new IllegalArgumentException (aBaseDir + " is not an existing directory");
    m_aBaseDir = aBaseDir;
    m_aTOCFile = new File (m_aBaseDir, "toc.xml");

    // Read the table of contents
    if (m_aTOCFile.exists ())
      if (XMLListHandler.readList (new FileSystemResource (m_aTOCFile), m_aAttachments).isFailure ())
        s_aLogger.error ("Failed to read TOC file " + m_aTOCFile);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <String> getAllAttachmentIDs ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aAttachments);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public final boolean containsAttachmentOfID (@Nullable final String sAttachmentID)
  {
    // TreeSet cannot cope with null keys!
    if (StringHelper.hasNoText (sAttachmentID))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aAttachments.contains (sAttachmentID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  private IProcAttachment _readAttachment (@Nonnull final String sAttachmentID)
  {
    final File aBaseDir = new File (m_aBaseDir, sAttachmentID);
    final IMicroDocument aMetaData = MicroReader.readMicroXML (new FileSystemResource (aBaseDir, FILENAME_METADATA_XML));
    if (aMetaData == null)
      throw new IllegalStateException ("MetaData of " + aBaseDir + " could not be read");
    final IMicroElement eRoot = aMetaData.getDocumentElement ();
    final String sUploadDT = eRoot.getAttribute (ATTR_UPLOADDT);
    final DateTime aUploadDT = sUploadDT == null ? PDTFactory.getCurrentDateTime ()
                                                : PDTFactory.createDateTimeFromMillis (StringParser.parseLong (sUploadDT,
                                                                                                               0));
    final IMicroElement eTitle = eRoot.getFirstChildElement (ELEMENT_TITLE);
    final IMicroElement eMimeType = eRoot.getFirstChildElement (ELEMENT_MIMETYPE);
    IMimeType aMimeType = null;
    if (eMimeType != null)
      aMimeType = MimeTypeParser.parseMimeType (eMimeType.getTextContent ());
    return new ProcResourceAttachment (sAttachmentID,
                                       eTitle.getTextContent (),
                                       aMimeType,
                                       aUploadDT,
                                       new FileSystemResource (aBaseDir, FILENAME_ATTACHMENT_DAT));
  }

  @Nullable
  public final IProcAttachment getAttachmentOfID (@Nullable final String sAttachmentID)
  {
    if (!containsAttachmentOfID (sAttachmentID))
      return null;

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Already resolved the attachment?
      IProcAttachment ret = m_aCache.get (sAttachmentID);
      if (ret == null)
      {
        // Not yet cached -> read and put in cache
        ret = _readAttachment (sAttachmentID);
        m_aCache.put (sAttachmentID, ret);
      }
      return ret;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Write the attachment content to disk
   * 
   * @param aAttachmentBaseDir
   *        The attachment directory
   * @param aAttachment
   *        The attachment itself
   * @return The resource referencing the stored attachment date
   */
  @Nonnull
  private static IReadableResource _persistAttachmentContent (@Nonnull final File aAttachmentBaseDir,
                                                              @Nonnull final IProcAttachment aAttachment)
  {
    // Determine the destination file
    final File aFile = new File (aAttachmentBaseDir, FILENAME_ATTACHMENT_DAT);

    // Save to disk
    final FileSystemResource aOSP = new FileSystemResource (aFile);
    StreamUtils.copyInputStreamToOutputStreamAndCloseOS (aAttachment.getInputStream (),
                                                         aOSP.getOutputStream (EAppend.DEFAULT));

    // Return reference
    return new FileSystemResource (aFile);
  }

  /**
   * Persist the attachment metadata to disk.
   * 
   * @param aAttachmentBaseDir
   *        The attachment base directory
   * @param aAttachment
   *        The attachment data
   */
  private static void _persistAttachmentMetaData (@Nonnull final File aAttachmentBaseDir,
                                                  @Nonnull final IProcAttachment aAttachment)
  {
    // Destination file
    final File aFile = new File (aAttachmentBaseDir, FILENAME_METADATA_XML);

    // Assemble XML content
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("metadata");
    eRoot.setAttribute (ATTR_ID, aAttachment.getID ());
    eRoot.setAttribute (ATTR_UPLOADDT, Long.toString (PDTFactory.getCurrentMillis ()));
    eRoot.appendElement (ELEMENT_TITLE).appendText (aAttachment.getTitle ());
    if (aAttachment.getMIMEType () != null)
      eRoot.appendElement (ELEMENT_MIMETYPE).appendText (aAttachment.getMIMEType ().getAsString ());

    // Save to file
    MicroWriter.writeToStream (aDoc, FileUtils.getOutputStream (aFile));
  }

  @Nonnull
  public final IProcAttachment persistAttachment (@Nonnull final IProcAttachment aAttachment)
  {
    if (aAttachment == null)
      throw new NullPointerException ("attachment");

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Small hint on double storage of attachments!
      if (aAttachment.isPersisted ())
        s_aLogger.warn ("Passed attachment is already persisted: " + aAttachment);

      final String sAttachmentID = aAttachment.getID ();
      final File aAttachmentBaseDir = new File (m_aBaseDir, sAttachmentID);
      final FileIOError eError = FileOperations.createDir (aAttachmentBaseDir);
      if (eError.isFailure ())
        throw new IllegalStateException ("Failed to create directory " + aAttachmentBaseDir + ": " + eError.toString ());

      // Persist the main items
      final IReadableResource aContentRes = _persistAttachmentContent (aAttachmentBaseDir, aAttachment);
      _persistAttachmentMetaData (aAttachmentBaseDir, aAttachment);

      // Add to TOC and persist
      m_aAttachments.add (sAttachmentID);
      XMLListHandler.writeList (m_aAttachments, FileUtils.getOutputStream (m_aTOCFile));

      s_aLogger.info ("Stored attachment " + sAttachmentID);

      // Return the link to the persisted resource
      return new ProcResourceAttachment (aAttachment.getID (),
                                         aAttachment.getTitle (),
                                         aAttachment.getMIMEType (),
                                         PDTFactory.getCurrentDateTime (),
                                         aContentRes);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange removeAttachment (@Nullable final String sAttachmentID)
  {
    if (!containsAttachmentOfID (sAttachmentID))
      return EChange.UNCHANGED;

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Simply delete the whole attachment directory
      final File aAttachmentBaseDir = new File (m_aBaseDir, sAttachmentID);
      final FileIOError eError = FileOperations.deleteDirRecursive (aAttachmentBaseDir);
      if (eError.isFailure ())
        throw new IllegalStateException ("Failed to deleted directory " +
                                         aAttachmentBaseDir +
                                         ": " +
                                         eError.toString ());

      // Remove from TOC and persist TOC
      m_aAttachments.remove (sAttachmentID);
      XMLListHandler.writeList (m_aAttachments, FileUtils.getOutputStream (m_aTOCFile));

      s_aLogger.info ("Removed attachment " + sAttachmentID);

      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }
}
