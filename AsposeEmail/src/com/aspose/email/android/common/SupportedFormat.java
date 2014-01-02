package com.aspose.email.android.common;

import java.util.LinkedHashMap;

import com.aspose.email.MessageFormat;
/**
 * Controls the File Formats that are to be supported by the application
 */
public class SupportedFormat 
{
	LinkedHashMap<String, MessageFormat> saveFormats;
	public static String formats[];
	public static MessageFormat[] formatValues;
	
	public void setFormats()
	{
		saveFormats = new LinkedHashMap<String, MessageFormat>();
		
		//===========================================================
		saveFormats.put("EML" , MessageFormat.getEml());
		saveFormats.put("EMLX", MessageFormat.getEmlx());
		saveFormats.put("MHT" , MessageFormat.getMht());
		saveFormats.put("MSG" , MessageFormat.getMsg());
		//===========================================================
		
		formats = saveFormats.keySet().toArray(new String[0]);
		formatValues = saveFormats.values().toArray(new MessageFormat[0]);
	}
}