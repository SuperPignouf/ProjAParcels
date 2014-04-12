/*
 * Code inspired by Sue Smith: see "http://code.tutsplus.com/tutorials/android-sdk-create-a-barcode-reader--mobile-17162"
 */

package activities;

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
	private TextView formatTxt, contentTxt, responseTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		scanBtn = (Button)findViewById(R.id.scan_button);
		formatTxt = (TextView)findViewById(R.id.scan_format);
		contentTxt = (TextView)findViewById(R.id.scan_content);
		responseTxt = (TextView)findViewById(R.id.scan_response);
		scanBtn.setOnClickListener(this);
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
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			formatTxt.setText("FORMAT: " + scanFormat);
			contentTxt.setText("CONTENT: " + scanContent);

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
				responseTxt.setText(response);
			}
		});	
	}

}
