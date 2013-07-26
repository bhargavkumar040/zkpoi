/* XSSFChart.java

	Purpose:
		
	Description:
		
	History:
		Oct 18, 2010 12:08:12 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.poi.xssf.usermodel;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;
import org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.WsDrDocument;
import org.zkoss.poi.POIXMLDocumentPart;
import org.zkoss.poi.openxml4j.opc.PackagePart;
import org.zkoss.poi.openxml4j.opc.PackageRelationship;
import org.zkoss.poi.ss.usermodel.Chart;
import org.zkoss.poi.ss.usermodel.ChartInfo;
import org.zkoss.poi.ss.usermodel.ClientAnchor;

/**
 * 
 * @author henrichen
 *
 */
public class XSSFChartX implements Chart {
	private String _chartId;
	private XSSFDrawing _patriarch;
	private XSSFClientAnchor _anchor;
	private String _name;

	public XSSFChartX(XSSFDrawing patriarch, XSSFClientAnchor anchor, String name, String chartId) {
		_patriarch = patriarch;
		_chartId = chartId;
		_anchor = anchor;
		_name = name;
	}

	@Override
	public ClientAnchor getPreferredSize() {
		return _anchor;
	}
	
	public ChartInfo getChartInfo() {
        for(POIXMLDocumentPart part : _patriarch.getRelations()){
	        if(part instanceof XSSFChart && part.getPackageRelationship().getId().equals(_chartId)){
	            return (XSSFChart)part;
	        }
        }
        return null;
	}
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}
}