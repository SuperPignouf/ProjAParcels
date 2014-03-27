package com.example.parcelsscanner;

import rest.RestClient;
import rest.RestClient.RequestMethod;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Button submit = null;

	private EditText matricule = null;
	private EditText password = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		boolean isConnected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
		if (isConnected) System.out.println("Internet available");
		else System.out.println("No Internet");
			//boolean wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
			//Log.d("NetworkState", "L'interface de connexion active est du Wifi : " + wifi);
		

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
			RestClient client =  new RestClient("http://localhost:8080/ParcelREST/rest/hello");
			client.AddParam("matricule", matricule.getText().toString());
			client.AddParam("password", password.getText().toString());
			client.setRequestType(RequestMethod.GET);
			try {
			    client.execute();
			} catch (Exception e) {
			    e.printStackTrace();
			}

			String response = client.getResponse();
		}
	};
}