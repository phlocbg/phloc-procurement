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
package com.phloc.procurement.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IMultiLingualText;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.poi.excel.ExcelReadUtils;
import com.phloc.procurement.codelist.classification.UNSPSCItem;
import com.phloc.procurement.codelist.classification.UNSPSCReader;

public final class MainObsoletedReadUNSPSC
{
  private static Map <String, String> _readGermanMapping () throws Exception
  {
    final Map <String, String> aMapToGerman = new HashMap <String, String> ();
    final IReadableResource aRes = new ClassPathResource ("/UNSPSC V9.1201_Deutsch_Codeonly_Sample.xls");
    final Workbook aWB = new HSSFWorkbook (aRes.getInputStream ());
    final Sheet aSheet = aWB.getSheetAt (0);
    final Iterator <Row> it = aSheet.rowIterator ();
    it.next ();

    while (it.hasNext ())
    {
      final Row aRow = it.next ();
      final String sCode = ExcelReadUtils.getCellValueString (aRow.getCell (0));
      final String sDE = ExcelReadUtils.getCellValueString (aRow.getCell (1));
      if (aMapToGerman.put (sCode, sDE) != null)
        throw new IllegalStateException ("Duplicate code for DE: " + sCode);
    }
    return aMapToGerman;
  }

  public static void main (final String [] args) throws Exception
  {
    final Map <String, String> aGermanMapping = _readGermanMapping ();

    final IReadableResource aRes = new ClassPathResource ("/UNSPSC V9.1201_ENGLISH_PUBLIC VERSION_Codeonly.xls");
    final Workbook aWB = new HSSFWorkbook (aRes.getInputStream ());
    final Sheet aSheet = aWB.getSheetAt (1);
    final Iterator <Row> it = aSheet.rowIterator ();
    it.next ();

    final Map <String, UNSPSCItem> aEntries = new HashMap <String, UNSPSCItem> ();
    while (it.hasNext ())
    {
      final Row aRow = it.next ();
      final String sKey = ExcelReadUtils.getCellValueString (aRow.getCell (0));
      final String sCode = ExcelReadUtils.getCellValueString (aRow.getCell (1));
      final String sTitle = ExcelReadUtils.getCellValueString (aRow.getCell (2));
      final String sDefinition = ExcelReadUtils.getCellValueString (aRow.getCell (3));

      if (StringHelper.hasNoText (sKey))
        break;

      final IMultiLingualText aTitle = new MultiLingualText ();
      aTitle.setText (TextProvider.EN, sTitle);
      final String sDE = aGermanMapping.get (sCode);
      if (StringHelper.hasText (sDE))
        aTitle.setText (TextProvider.DE, sDE);

      final IMultiLingualText aDefinition = new MultiLingualText ();
      if (StringHelper.hasText (sDefinition))
        aDefinition.setText (TextProvider.EN, sDefinition);

      if (aEntries.put (sCode, new UNSPSCItem (sKey, sCode, aTitle, aDefinition.isEmpty () ? null : aDefinition)) != null)
        throw new IllegalStateException ("Duplicate code: " + sCode);
    }

    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (UNSPSCReader.ELEMENT_ROOT);
    for (final Map.Entry <String, UNSPSCItem> aEntry : ContainerHelper.getSortedByKey (aEntries).entrySet ())
    {
      final UNSPSCItem aItem = aEntry.getValue ();
      final IMicroElement eCode = eRoot.appendElement (UNSPSCReader.ELEMENT_ITEM);
      eCode.setAttribute (UNSPSCReader.ATTR_CODE, aItem.getCode ());
      eCode.setAttribute (UNSPSCReader.ATTR_KEY, aItem.getKey ());
      eCode.appendChild (MicroTypeConverter.convertToMicroElement (aItem.getTitle (), UNSPSCReader.ELEMENT_TITLE));
      if (aItem.getDefinition () != null)
        eCode.appendChild (MicroTypeConverter.convertToMicroElement (aItem.getDefinition (),
                                                                     UNSPSCReader.ELEMENT_DEFINITION));
    }

    SimpleFileIO.writeFile (new File ("src/main/resources/codelists/unspsc.xml"),
                            MicroWriter.getXMLString (aDoc),
                            CCharset.CHARSET_UTF_8);
  }
}
