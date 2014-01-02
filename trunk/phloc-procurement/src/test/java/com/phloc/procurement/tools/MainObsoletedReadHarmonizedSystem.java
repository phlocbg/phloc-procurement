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
import com.phloc.procurement.codelist.classification.HarmonizedSystemItem;
import com.phloc.procurement.codelist.classification.HarmonizedSystemReader;

public final class MainObsoletedReadHarmonizedSystem
{
  public static void main (final String [] args) throws Exception
  {
    final IReadableResource aRes = new ClassPathResource ("/Harmonized_System_English.xls");
    final Workbook aWB = new HSSFWorkbook (aRes.getInputStream ());
    final Sheet aSheet = aWB.getSheetAt (0);
    final Iterator <Row> it = aSheet.rowIterator ();
    it.next ();

    final Map <String, HarmonizedSystemItem> aEntries = new HashMap <String, HarmonizedSystemItem> ();
    while (it.hasNext ())
    {
      final Row aRow = it.next ();
      final String sCode = ExcelReadUtils.getCellValueString (aRow.getCell (0));
      final String sTitle = ExcelReadUtils.getCellValueString (aRow.getCell (1));

      if (StringHelper.hasNoText (sCode))
        continue;

      final IMultiLingualText aTitle = new MultiLingualText ();
      aTitle.setText (TextProvider.EN, sTitle);

      if (aEntries.put (sCode, new HarmonizedSystemItem (sCode, aTitle)) != null)
        throw new IllegalStateException ("Duplicate code: " + sCode);
    }

    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (HarmonizedSystemReader.ELEMENT_ROOT);
    for (final Map.Entry <String, HarmonizedSystemItem> aEntry : ContainerHelper.getSortedByKey (aEntries).entrySet ())
    {
      final HarmonizedSystemItem aItem = aEntry.getValue ();
      final IMicroElement eCode = eRoot.appendElement (HarmonizedSystemReader.ELEMENT_ITEM);
      eCode.setAttribute (HarmonizedSystemReader.ATTR_CODE, aItem.getCode ());
      eCode.appendChild (MicroTypeConverter.convertToMicroElement (aItem.getTitle (),
                                                                   HarmonizedSystemReader.ELEMENT_TITLE));
    }

    SimpleFileIO.writeFile (new File ("src/main/resources/codelists/harmonized_system.xml"),
                            MicroWriter.getXMLString (aDoc),
                            CCharset.CHARSET_UTF_8_OBJ);
  }
}
