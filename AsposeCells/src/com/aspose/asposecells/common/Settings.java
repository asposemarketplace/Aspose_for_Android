package com.aspose.asposecells.common;

import com.aspose.asposecells.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity 
{
	public static final String KEY_APP = "app_key";
	public static final String KEY_SID = "app_sid";
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}