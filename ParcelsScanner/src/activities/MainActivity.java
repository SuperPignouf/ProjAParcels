package activities;

import com.example.parcelsscanner.R;

import rest.RestClient;
import rest.RestClient.RequestMethod;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button submit = null;

	private EditText matricule = null;
	private EditText password = null;

	private MainActivity MA;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MA = this;

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		boolean isConnected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
		if (isConnected) System.out.println("Internet available");
		else System.out.println("No Internet");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// On récupère toutes les vues dont on a besoin
		submit = (Button)findViewById(R.id.submit);

		matricule = (EditText)findViewById(R.id.matricule);
		password = (EditText)findViewById(R.id.password);


		// On attribue un listener adapté aux vues qui en ont besoin
		submit.setOnClickListener(envoyerListener);

	}

	// Uniquement pour le bouton "envoyer"
	private OnClickListener envoyerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String mat = matricule.getText().toString();
			String pass = password.getText().toString();
			if (mat.matches("") || pass.matches("")) Toast.makeText(MA, "Please enter matricule and password", Toast.LENGTH_SHORT).show();
			else{
				System.out.println("http://" + getString(R.string.restIP) + "/ParcelREST/rest/hello");
				RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/hello", MA);
				client.AddParam("matricule", mat);
				client.AddParam("password", pass);
				client.setRequestType(RequestMethod.GET);
				try {
					client.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void notifyResult(String response, RequestMethod requestType) {
		System.out.println(response);

		if (requestType.equals(RequestMethod.GET)){
			if (response.contains("1")){
				Intent intent = new Intent(getBaseContext(), ScanActivity.class);
                startActivity(intent);   
			}
			else if (response.contains("0")){
				final MainActivity MA = this;
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MA, "Login failed !", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}

	}
}