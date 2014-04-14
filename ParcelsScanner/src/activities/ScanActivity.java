/*
 * Code inspired by Sue Smith: see "http://code.tutsplus.com/tutorials/android-sdk-create-a-barcode-reader--mobile-17162"
 */

package activities;

import java.util.Scanner;

import rest.RestClient;
import rest.RestClient.RequestMethod;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parcelsscanner.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends Activity implements OnClickListener {

	private Button scanBtn;
	private TextView formatTxt, contentTxt, responseTxt, statusBtn;
	private String scanContent, scanFormat;
	private int state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		scanBtn = (Button)findViewById(R.id.scan_button);
		formatTxt = (TextView)findViewById(R.id.scan_format);
		contentTxt = (TextView)findViewById(R.id.scan_content);
		responseTxt = (TextView)findViewById(R.id.scan_response);
		statusBtn = (Button)findViewById(R.id.scan_status);
		statusBtn.setVisibility(View.INVISIBLE);
		scanBtn.setOnClickListener(this);
		statusBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId()==R.id.scan_button){
			state = 1;
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}
		if(arg0.getId()==R.id.scan_status){
			state = 2;
			System.out.println("Creating REST CLIENT: http://" + getString(R.string.restIP) + "/ParcelREST/rest/update");
			RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/update", this);
			client.AddParam("format", this.scanFormat);
			client.AddParam("content", this.scanContent);
			client.setRequestType(RequestMethod.GET);
			try {
				client.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			this.scanContent = scanningResult.getContents();
			this.scanFormat = scanningResult.getFormatName();
			this.formatTxt.setText("FORMAT: " + scanFormat);
			this.contentTxt.setText("CONTENT: " + scanContent);

			System.out.println("http://" + getString(R.string.restIP) + "/ParcelREST/rest/scan");
			RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/scan", this);
			client.AddParam("format", scanFormat);
			client.AddParam("content", scanContent);
			client.setRequestType(RequestMethod.GET);
			try {
				client.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void notifyResult(final String response) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				if (state == 2){
					statusBtn.setVisibility(View.INVISIBLE);
					Toast toast = Toast.makeText(getApplicationContext(),
							"New status is " + response, Toast.LENGTH_LONG);
					toast.show();
					Scanner scan = new Scanner(responseTxt.getText().toString());
					scan.useDelimiter("\n");
					String result = scan.next();
					result += "\n Current status: " + response;
					responseTxt.setText(result);
					scan.close();
				}
				else{
					responseTxt.setText(response);
					statusBtn.setVisibility(View.VISIBLE);
				}
			}
		});	
	}

}
