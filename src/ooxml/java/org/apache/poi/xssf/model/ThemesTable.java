/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.zkoss.poi.xssf.model;

import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
import org.zkoss.poi.POIXMLDocumentPart;
import org.zkoss.poi.openxml4j.opc.PackagePart;
import org.zkoss.poi.openxml4j.opc.PackageRelationship;
import org.zkoss.poi.xssf.usermodel.XSSFColor;

/**
 * Class that represents theme of XLSX document. The theme includes specific
 * colors and fonts.
 * 
 * @author Petr Udalau(Petr.Udalau at exigenservices.com) - theme colors
 */
public class ThemesTable extends POIXMLDocumentPart {
    private ThemeDocument theme;

    public ThemesTable(PackagePart part, PackageRelationship rel) throws Exception {
        super(part, rel);
        theme = ThemeDocument.Factory.parse(part.getInputStream());
    }

    public ThemesTable(ThemeDocument theme) {
        this.theme = theme;
    }

    public XSSFColor getThemeColor(int idx) {
        CTColorScheme colorScheme = theme.getTheme().getThemeElements().getClrScheme();
        //20101123, henrichen@zkoss.org: shall handle System Color case
        if (idx == 0) {
        	return new XSSFColor(colorScheme.getLt1().getSysClr().getLastClr());
        } else if (idx == 1) {
        	return new XSSFColor(colorScheme.getDk1().getSysClr().getLastClr());
        }
        CTColor ctColor = null;
        int cnt = 0;
        for (XmlObject obj : colorScheme.selectPath("./*")) {
            if (obj instanceof org.openxmlformats.schemas.drawingml.x2006.main.CTColor) {
                if (cnt == idx) {
                    ctColor = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor) obj;
                    CTSRgbColor srgbClr = ctColor.getSrgbClr();
                    if (srgbClr != null)
                    	return new XSSFColor(srgbClr.getVal());
                    else
                    	break;
                }
                cnt++;
            }
        }
        return null;
    }
}