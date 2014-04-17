/*
 * Code inspired by Sue Smith: see "http://code.tutsplus.com/tutorials/android-sdk-create-a-barcode-reader--mobile-17162"
 * and by "http://androidworkshop.tumblr.com/"
 */

package activities;

import java.util.Scanner;

import nfc.NFCForegroundUtil;
import rest.RestClient;
import rest.RestClient.RequestMethod;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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

	private NFCForegroundUtil nfcForegroundUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		this.scanBtn = (Button)findViewById(R.id.scan_button);
		this.formatTxt = (TextView)findViewById(R.id.scan_format);
		this.contentTxt = (TextView)findViewById(R.id.scan_content);
		this.responseTxt = (TextView)findViewById(R.id.scan_response);
		this.statusBtn = (Button)findViewById(R.id.scan_status);
		this.statusBtn.setVisibility(View.INVISIBLE);
		this.scanBtn.setOnClickListener(this);
		this.statusBtn.setOnClickListener(this);

		this.nfcForegroundUtil = new NFCForegroundUtil(this);
	}

	public void onPause() {
		super.onPause();
		this.nfcForegroundUtil.disableForeground();
	}  

	public void onResume() {
		super.onResume();
		this.nfcForegroundUtil.enableForeground();

		if (!this.nfcForegroundUtil.getNfc().isEnabled())
		{
			Toast.makeText(getApplicationContext(), 
					"Please activate NFC and press Back to return to the application!", 
					Toast.LENGTH_LONG).show();
			startActivity(
					new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}

	}

	public void onNewIntent(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		this.state = 1;
		this.scanContent = bytesToHex(tag.getId());
		this.scanFormat = "RFID";
		this.formatTxt.setText("FORMAT: " + scanFormat);
		this.contentTxt.setText("CONTENT: " + scanContent);
		this.responseTxt.setText("");
		statusBtn.setVisibility(View.VISIBLE);
		
		System.out.println("http://" + getString(R.string.restIP) + "/ParcelREST/rest/scan");
		RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/scan", this);
		client.AddParam("format", scanFormat);
		client.AddParam("content", scanContent.replace(" ", ""));
		client.setRequestType(RequestMethod.GET);
		try {
			client.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Convenience method to convert a byte array to a hex string.
	 *
	 * @param  data  the byte[] to convert
	 * @return String the converted byte[]
	 */

	private String bytesToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buf.append(byteToHex(data[i]).toUpperCase());
			buf.append(" ");
		}
		return (buf.toString());
	}

	/**
	 *  method to convert a byte to a hex string.
	 *
	 * @param  data  the byte to convert
	 * @return String the converted byte
	 */
	private String byteToHex(byte data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	/**
	 *  Convenience method to convert an int to a hex char.
	 *
	 * @param  i  the int to convert
	 * @return char the converted char
	 */
	private char toHexChar(int i) {
		if ((0 <= i) && (i <= 9)) {
			return (char) ('0' + i);
		} else {
			return (char) ('a' + (i - 10));
		}
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
			client.AddParam("content", this.scanContent.replace(" ", ""));
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
			this.contentTxt.setText("CONTENT: " + scanContent.replace(" ", ""));
			this.responseTxt.setText("");

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
