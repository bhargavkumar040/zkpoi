/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

package org.zkoss.poi.xssf.usermodel.charts;

import org.zkoss.poi.ss.usermodel.charts.ChartDataSource;
import org.zkoss.poi.ss.usermodel.charts.ChartTextSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

/**
 * Package private class with utility methods.
 *
 * @author Roman Kashitsyn
 */
class XSSFChartUtil {

    private XSSFChartUtil() {}

    /**
     * Builds CTAxDataSource object content from POI ChartDataSource.
     * @param ctAxDataSource OOXML data source to build
     * @param dataSource POI data source to use
     */
    public static void buildAxDataSource(CTAxDataSource ctAxDataSource, ChartDataSource<?> dataSource) {
        if (dataSource.isNumeric()) {
            if (dataSource.isReference()) {
                buildNumRef(ctAxDataSource.addNewNumRef(), dataSource);
            } else {
                buildNumLit(ctAxDataSource.addNewNumLit(), dataSource);
            }
        } else {
            if (dataSource.isReference()) {
                buildStrRef(ctAxDataSource.addNewStrRef(), dataSource);
            } else {
                buildStrLit(ctAxDataSource.addNewStrLit(), dataSource);
            }
        }
    }

    /**
     * Builds CTNumDataSource object content from POI ChartDataSource
     * @param ctNumDataSource OOXML data source to build
     * @param dataSource POI data source to use
     */
    public static void buildNumDataSource(CTNumDataSource ctNumDataSource,
                                          ChartDataSource<? extends Number> dataSource) {
        if (dataSource.isReference()) {
            buildNumRef(ctNumDataSource.addNewNumRef(), dataSource);
        } else {
            buildNumLit(ctNumDataSource.addNewNumLit(), dataSource);
        }
    }

    private static void buildNumRef(CTNumRef ctNumRef, ChartDataSource<?> dataSource) {
        ctNumRef.setF(dataSource.getFormulaString());
        CTNumData cache = ctNumRef.addNewNumCache();
        fillNumCache(cache, dataSource);
    }

    private static void buildNumLit(CTNumData ctNumData, ChartDataSource<?> dataSource) {
        fillNumCache(ctNumData, dataSource);
    }

    private static void buildStrRef(CTStrRef ctStrRef, ChartDataSource<?> dataSource) {
        ctStrRef.setF(dataSource.getFormulaString());
        CTStrData cache = ctStrRef.addNewStrCache();
        fillStringCache(cache, dataSource);
    }

    private static void buildStrLit(CTStrData ctStrData, ChartDataSource<?> dataSource) {
        fillStringCache(ctStrData, dataSource);
    }

    private static void fillStringCache(CTStrData cache, ChartDataSource<?> dataSource) {
        int numOfPoints = dataSource.getPointCount();
        cache.addNewPtCount().setVal(numOfPoints);
        for (int i = 0; i < numOfPoints; ++i) {
            Object value = dataSource.getPointAt(i);
            if (value != null) {
                CTStrVal ctStrVal = cache.addNewPt();
                ctStrVal.setIdx(i);
                ctStrVal.setV(value.toString());
            }
        }

    }

    private static void fillNumCache(CTNumData cache, ChartDataSource<?> dataSource) {
        int numOfPoints = dataSource.getPointCount();
        cache.addNewPtCount().setVal(numOfPoints);
        for (int i = 0; i < numOfPoints; ++i) {
            Number value = (Number) dataSource.getPointAt(i);
            if (value != null) {
                CTNumVal ctNumVal = cache.addNewPt();
                ctNumVal.setIdx(i);
                ctNumVal.setV(value.toString());
            }
        }
    }
    
    
    //20111006, henrichen@zkoss.org: handle serie title text
    /**
     * Builds CTSerTx object content from POI ChartDataSource.
     * @param ctSerTx OOXML serie text source to build
     * @param textSource POI text source to use
     */
    public static void buildSerTx(CTSerTx ctSerTx, ChartTextSource textSource) {
    	final String value = textSource.getFormulaString();
        if (textSource.isReference()) {
        	buildStrRef(ctSerTx.addNewStrRef(), textSource);
        } else {
            ctSerTx.setV(textSource.getTextString());
        }
    }
    //20111006, henrichen@zkoss.org: handle serie title text
    private static void buildStrRef(CTStrRef ctStrRef, ChartTextSource textSource) {
        ctStrRef.setF(textSource.getFormulaString());
        CTStrData cache = ctStrRef.addNewStrCache();
        fillStringCache(cache, textSource);
    }
    //20111006, henrichen@zkoss.org: handle serie title text
    private static void fillStringCache(CTStrData cache, ChartTextSource textSource) {
        cache.addNewPtCount().setVal(1);
        CTStrVal ctStrVal = cache.addNewPt();
        ctStrVal.setIdx(0);
        ctStrVal.setV(textSource.getTextString());
    }
}
