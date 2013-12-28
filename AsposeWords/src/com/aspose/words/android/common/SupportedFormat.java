package com.aspose.words.android.common;

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
		saveFormats.put("PDF", com.aspose.words.SaveFormat.PDF);
		saveFormats.put("HTML", com.aspose.words.SaveFormat.HTML);
		saveFormats.put("JPG", com.aspose.words.SaveFormat.JPEG);
		//===========================================================
		
		formats = saveFormats.keySet().toArray(new String[0]);
		formatValues = saveFormats.values().toArray(new Integer[0]);
	}
}