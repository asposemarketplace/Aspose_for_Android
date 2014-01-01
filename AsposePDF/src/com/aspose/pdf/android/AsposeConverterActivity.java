package com.aspose.pdf.android;

import java.io.File;

import com.aspose.pdf.Document;
import com.aspose.pdf.android.common.BrowseFileActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

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
		btn_browse_file1.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				startActivityForResult(new Intent(AsposeConverterActivity.this, BrowseFileActivity.class), PATH);
			}
		});
		btn_browse_file2.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				startActivityForResult(new Intent(AsposeConverterActivity.this, BrowseFileActivity.class), PATH_2);
			}
		});
		//==================================================================
		btn_concat.setOnClickListener(new View.OnClickListener() 
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
		String inputPath_file1;
		String inputPath_file2;
		String outputPath;
		String outputFileName;
		ProgressDialog progDialog;

		@Override
		protected void onPreExecute()
		{
			showProgressDialog();

			inputPath_file1 = tv_inputPath_file1.getText().toString();
			inputPath_file2 = tv_inputPath_file2.getText().toString();
			
			System.out.println("inputPath_file1: " + inputPath_file1);
			System.out.println("inputPath_file2: " + inputPath_file2);
			
			Calendar c = Calendar.getInstance(); 
			outputFileName = "AsposeMerged_" + c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND)+".pdf";
			outputPath = getFileSaveLocation() + outputFileName;
		}
		
		//==================================================================
		@Override
		protected Boolean doInBackground(String... params)
		{
			try
			{
				//open first document
				Document pdfDocument1 = new Document(inputPath_file1);
				//open second document
				Document pdfDocument2 = new Document(inputPath_file2);
				//add pages of second document to the first
				pdfDocument1.getPages().add(pdfDocument2.getPages());

				//save concatenated output file
				pdfDocument1.save(outputPath);				
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
				Toast.makeText(getApplicationContext(), "Concat Successful.", Toast.LENGTH_SHORT).show();
				tv_result.setText(	"Files merged and saved as \"" + outputPath + "\"");
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Concat Failed.", Toast.LENGTH_SHORT).show();
				tv_result.setText(R.string.msg_oops);
			}
			progDialog.dismiss();
		}

		//==================================================================
		private void showProgressDialog()
		{
			progDialog = new ProgressDialog(AsposeConverterActivity.this);
			progDialog.setMessage("Concatenating your docs...");
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
			System.out.println("Path for File 1");
			if (resultCode == RESULT_OK)
			{
				String path = data.getStringExtra("path");
				if (path == null)
				{
					Toast.makeText(AsposeConverterActivity.this, "Path Not Found", Toast.LENGTH_LONG).show();
				}
				else
				{
					tv_inputPath_file1.setText(path);
				}
			}
		}
		else if (requestCode == PATH_2)
		{
			System.out.println("Path for File 2");
			if (resultCode == RESULT_OK)
			{
				String path = data.getStringExtra("path");
				if (path == null)
				{
					Toast.makeText(AsposeConverterActivity.this, "Path Not Found", Toast.LENGTH_LONG).show();
				}
				else
				{
					tv_inputPath_file2.setText(path);
				}
			}
		}
		enableFunctionBtn();
	}
	//======================================================================
	private void enableFunctionBtn()
	{
		if (tv_inputPath_file1.getText().toString().length() != 0 && tv_inputPath_file2.getText().toString().length() != 0)
		{
			btn_concat.setEnabled(true);
		}
		else
		{
			btn_concat.setEnabled(false);
		}
	}
	//======================================================================
	/**
	 * Initialized the basic need for conversion activity
	 */
	private void initialize()
	{		
		//======= UI Controls ==================================
		tv_result = (TextView) findViewById(R.id.tv_result);
		tv_inputPath_file1 = (TextView) findViewById(R.id.input_path_1);
		tv_inputPath_file2 = (TextView) findViewById(R.id.input_path_2);

		btn_browse_file1 = (Button) findViewById(R.id.btn_browse_file1);
		btn_browse_file2 = (Button) findViewById(R.id.btn_browse_file2);
		
		btn_concat = (Button) findViewById(R.id.btn_concat);
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
	TextView tv_inputPath_file1;
	TextView tv_inputPath_file2;
	TextView tv_result;
	Button btn_browse_file1;
	Button btn_browse_file2;
	Button btn_concat;

	int outputFormat; // format for the converted file
	String outputExt = "pdf"; // file extension for output file.

	protected static final int PATH = 1;
	protected static final int PATH_2 = 2;
	protected static final String OUTPUT_FOLDER = "AsposeFiles";
}