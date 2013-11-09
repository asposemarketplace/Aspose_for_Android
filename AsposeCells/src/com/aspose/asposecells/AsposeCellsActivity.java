package com.aspose.asposecells;

import org.apache.log4j.BasicConfigurator;

import com.aspose.asposecells.common.BrowseFileActivity;
import com.aspose.asposecells.common.Settings;
import com.aspose.cloud.sdk.common.AsposeApp;
import com.aspose.cloud.sdk.common.Product;

import com.aspose.cloud.sdk.cells.SaveFormat;
import com.aspose.cloud.sdk.cells.Converter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AsposeCellsActivity extends Activity
{
	TextView tv_inputPath;
	TextView tv_result;
	Button btn_browse;
	Button btn_convert;
	Spinner spinner_format;

	SaveFormat outputFormat;

	protected static final int PATH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		BasicConfigurator.configure();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aspose_cells);

		initialize();

		// =================================================================
		btn_browse.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				startActivityForResult(new Intent(AsposeCellsActivity.this, BrowseFileActivity.class), PATH);
			}
		});
		// =================================================================
		spinner_format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> prent, View view, int position, long id)
			{
				outputFormat = (SaveFormat) spinner_format.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				Toast.makeText(AsposeCellsActivity.this, "Please Select Save Format", Toast.LENGTH_LONG).show();
			}
		});
		// =================================================================
		btn_convert.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				if (setAppKeys())
				{
					new LocalFileConverterTask().execute();
				}
				else
				{
					startActivity(new Intent(AsposeCellsActivity.this, Settings.class));
				}
			}
		});
	}
	// ====================== Server Communication==========================
	class LocalFileConverterTask extends AsyncTask<String, Integer, Boolean>
	{
		String inputPath;
		String outputPath;
		ProgressDialog progDialog;

		@Override
		protected void onPreExecute()
		{
			showProgressDialog();

			inputPath = tv_inputPath.getText().toString();
			outputPath = inputPath.substring(inputPath.lastIndexOf('/') + 1, inputPath.lastIndexOf('.')) + "." + outputFormat;
		}
		
		// =================================================================
		@Override
		protected Boolean doInBackground(String... params)
		{
			try
			{
				boolean result = new Converter().convertLocalFile(inputPath, outputPath, outputFormat);
				return result;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		// =================================================================
		@Override
		protected void onPostExecute(Boolean result)
		{
			System.out.println("On Post Execute: " + result);
			if (result)
			{
				Toast.makeText(getApplicationContext(), "Conversion Successful.", Toast.LENGTH_SHORT).show();
				tv_result.setText("File converted and downloaded as " + outputPath + " in 'AsposeConvertedFiles' folder in your sdcard");
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Conversion Failed.", Toast.LENGTH_SHORT).show();
				tv_result.setText(R.string.msg_oops);
			}
			progDialog.dismiss();
		}

		// =================================================================
		private void showProgressDialog()
		{
			progDialog = new ProgressDialog(AsposeCellsActivity.this);
			progDialog.setMessage("Converting your document...");
			progDialog.setIndeterminate(false);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setCancelable(true);
			progDialog.show();
		}
	}

	// =====================================================================
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == PATH)
		{
			if (resultCode == RESULT_OK)
			{
				String path = data.getStringExtra("path");
				if (path == null)
				{
					Toast.makeText(AsposeCellsActivity.this, "Path Not Found", Toast.LENGTH_LONG).show();
					btn_convert.setEnabled(false);

				}
				else
				{
					tv_inputPath.setText(path);
					btn_convert.setEnabled(true);
				}
			}
		}
	}

	// =====================================================================
	/**
	 * Initialized the basic need for activity conversion
	 */
	private void initialize()
	{
		// === UI Controls
		tv_result = (TextView) findViewById(R.id.tv_result);
		tv_inputPath = (TextView) findViewById(R.id.input_path);
		btn_browse = (Button) findViewById(R.id.btn_browse);
		btn_convert = (Button) findViewById(R.id.btn_convert);

		spinner_format = (Spinner) findViewById(R.id.format_spinner);
		spinner_format.setAdapter(new ArrayAdapter<SaveFormat>(AsposeCellsActivity.this, android.R.layout.simple_list_item_1, SaveFormat.values()));

		outputFormat = (SaveFormat) spinner_format.getItemAtPosition(0);

		// === Additional Settings
		if (!setAppKeys())
		{
			startActivity(new Intent(AsposeCellsActivity.this, Settings.class));
		}
	}

	// =====================================================================
	private boolean setAppKeys()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		String app_sid = sp.getString("app_sid", "");
		String app_key = sp.getString("app_key", "");

		if (app_sid.equals("") || app_key.equals(""))
		{
			Toast.makeText(this, R.string.msg_set_appkeys, Toast.LENGTH_LONG).show();
			if (tv_result != null)
			{
				tv_result.setText(R.string.msg_set_appkeys);
			}

			return false;
		}
		else
		{
			AsposeApp.setAppInfo(app_key, app_sid);
			Product.setBaseProductUri("http://api.aspose.com/v1.1");

			System.out.println("=== Base URI and App Info set");

			if (tv_result != null)
			{
				tv_result.setText("");
			}
			return true;
		}
	}

	// =====================================================================

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aspose_cells, menu);
		return true;
	}

	// =====================================================================

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() > 0)
		{
			startActivity(new Intent(AsposeCellsActivity.this, Settings.class));
			return true;
		}
		return false;

	}
}