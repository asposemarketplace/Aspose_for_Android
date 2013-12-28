package com.aspose.words.android;

import java.io.File;

import com.aspose.words.android.common.BrowseFileActivity;
import com.aspose.words.android.common.SupportedFormat;

import com.aspose.words.Document;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AsposeConverterActivity extends Activity
{
	//======================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//BasicConfigurator.configure();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aspose_converter);

		initialize();

		//==================================================================
		btn_browse.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				startActivityForResult(new Intent(AsposeConverterActivity.this, BrowseFileActivity.class), PATH);
			}
		});
		//==================================================================
		spinner_format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> prent, View view, int position, long id)
			{
				outputFormat = SupportedFormat.formatValues[position];
				outputExt = SupportedFormat.formats[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				Toast.makeText(AsposeConverterActivity.this, "Please Select Save Format", Toast.LENGTH_LONG).show();
			}
		});
		//==================================================================
		btn_convert.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
					new LocalFileConverterTask().execute();
			}
		});
	}

	//======================================================================
	/**
	 * AsyncTask to convert the provided file in provided format if supported. 
	 */
	class LocalFileConverterTask extends AsyncTask<String, Integer, Boolean>
	{
		String inputPath;
		String outputPath;
		String outputFileName;
		ProgressDialog progDialog;

		@Override
		protected void onPreExecute()
		{
			showProgressDialog();

			inputPath = tv_inputPath.getText().toString();
			outputFileName = inputPath.substring(inputPath.lastIndexOf('/') + 1, inputPath.lastIndexOf('.')) + "." + outputExt;
			outputPath = getFileSaveLocation() + outputFileName;
		}
		
		//==================================================================
		@Override
		protected Boolean doInBackground(String... params)
		{
			try
			{
				Document doc = new Document(inputPath);				
				doc.save(outputPath, outputFormat);
				
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		//==================================================================
		@Override
		protected void onPostExecute(Boolean result)
		{
			if (result)
			{
				Toast.makeText(getApplicationContext(), "Conversion Successful.", Toast.LENGTH_SHORT).show();
				tv_result.setText(	"File converted and saved as \"" + outputPath + "\"");
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Conversion Failed.", Toast.LENGTH_SHORT).show();
				tv_result.setText(R.string.msg_oops);
			}
			progDialog.dismiss();
		}

		//==================================================================
		private void showProgressDialog()
		{
			progDialog = new ProgressDialog(AsposeConverterActivity.this);
			progDialog.setMessage("Converting your document...");
			progDialog.setIndeterminate(false);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setCancelable(true);
			progDialog.show();
		}
	}

	//======================================================================
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
					Toast.makeText(AsposeConverterActivity.this, "Path Not Found", Toast.LENGTH_LONG).show();
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

	//======================================================================
	/**
	 * Initialized the basic need for conversion activity
	 */
	private void initialize()
	{
		new SupportedFormat().setFormats();
		
		//======= UI Controls ==================================
		tv_result = (TextView) findViewById(R.id.tv_result);
		tv_inputPath = (TextView) findViewById(R.id.input_path);
		btn_browse = (Button) findViewById(R.id.btn_browse);
		btn_convert = (Button) findViewById(R.id.btn_convert);

		spinner_format = (Spinner) findViewById(R.id.format_spinner);
		
		spinner_format.setAdapter(
				new ArrayAdapter<String>(AsposeConverterActivity.this, 
				android.R.layout.simple_list_item_1, 
				SupportedFormat.formats));

		outputFormat = SupportedFormat.formatValues[0];
		outputExt = SupportedFormat.formats[0];
	}

	//======================================================================
	/**
	 * Sets the location to save the converted files.
	 */
	private String getFileSaveLocation()
	{
		String sdCardPath = Environment.getExternalStorageDirectory().getPath()+ File.separator + OUTPUT_FOLDER + File.separator;
		System.out.println("sdCardPath: " + sdCardPath);
		return sdCardPath;
	}
	//======================================================================
	
	// Class Variables
	TextView tv_inputPath;
	TextView tv_result;
	Button btn_browse;
	Button btn_convert;
	Spinner spinner_format;

	int outputFormat; // format for the converted file
	String outputExt; // file extension for output file.

	protected static final int PATH = 1;
	protected static final String OUTPUT_FOLDER = "AsposeFiles";
}