package com.aspose.cells.android.common;

import java.util.LinkedHashMap;
/**
 * Controls the File Formats that are to be supported by the application
 */
public class SupportedFormat 
{
	LinkedHashMap<String, Integer> saveFormats;
	public static String formats[];
	public static Integer formatValues[];
	
	public void setFormats()
	{
		saveFormats = new LinkedHashMap<String, Integer>();
		
		//===========================================================
		saveFormats.put("CSV", com.aspose.cells.FileFormatType.CSV);
		saveFormats.put("PDF", com.aspose.cells.FileFormatType.PDF);
		saveFormats.put("HTML", com.aspose.cells.FileFormatType.HTML);
		//===========================================================
		
		formats = saveFormats.keySet().toArray(new String[0]);
		formatValues = saveFormats.values().toArray(new Integer[0]);
	}
}