/**
 * Ecran principal de l'application Android: permet de scanner des codes-barres (en faisant appel a Zxing) et des puces RFID.
 * Affiche les informations des colis scannes et permet a l'utilisateur de commander la mise a jour du statut de colis.
 * 
 * Code tire en partie de Sue Smith: "http://code.tutsplus.com/tutorials/android-sdk-create-a-barcode-reader--mobile-17162"
 * et de "http://androidworkshop.tumblr.com/"
 */

package activities;

import geolocation.CurrentLocation;

import java.util.Scanner;

import nfc.NFCForegroundUtil;
import rest.RestClient;
import rest.RestClient.RequestMethod;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parcelsscanner.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends Activity implements OnClickListener {

	/**
	 * Bouton permettant de scanner un code-barre.
	 */
	private Button scanBtn;
	
	/**
	 * Bouton permettant de mettre a jour le statut d'un colis prealablement scanne.
	 */
	private Button statusBtn;	
	
	/**
	 * Champs de texte divers permettant d'afficher les infos de colis.
	 */
	private TextView formatTxt, contentTxt, responseTxt;
	
	/**
	 * String's permettant de conserver le resultat d'un scan.
	 */
	private String scanContent, scanFormat;
	
	/**
	 * Entier permettant de retenir si le dernier bouton enfonce est SCAN (state = 1) ou UPDATE STATUS (state = 2).
	 * On reagit differemment lors d'une notification de resultat en fonction de state.
	 * @see #onClick(View)
	 * @see #onNewIntent(Intent)
	 * @see #notifyResult(String)
	 */
	private int state;

	/**
	 * Permet de gerer NFC.
	 */
	private NFCForegroundUtil nfcForegroundUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		// On recupere les vues.
		this.scanBtn = (Button)findViewById(R.id.scan_button);
		this.formatTxt = (TextView)findViewById(R.id.scan_format);
		this.contentTxt = (TextView)findViewById(R.id.scan_content);
		this.responseTxt = (TextView)findViewById(R.id.scan_response);
		this.statusBtn = (Button)findViewById(R.id.scan_status);
		
		// Le bouton de mise a jour de statut doit etre invisible tant que rien n'a ete scanne
		this.statusBtn.setVisibility(View.INVISIBLE);
		
		//Attribution des listeners.
		this.scanBtn.setOnClickListener(this);
		this.statusBtn.setOnClickListener(this);

		// Initialisation de l'objet gerant NFC.
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

	/**
	 * Cette methode est appelee automatiquement lorsqu'une puce RFID est detectee a proximite
	 */
	public void onNewIntent(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		this.state = 1; // La derniere action est un scan
		this.scanContent = bytesToHex(tag.getId());
		this.scanFormat = "RFID";
		//Affichage du resultat du scan
		this.formatTxt.setText("FORMAT: " + scanFormat);
		this.contentTxt.setText("CONTENT: " + scanContent);
		this.responseTxt.setText("");
		statusBtn.setVisibility(View.VISIBLE); // Le bouton de mise a jour de statut est rendu visible
		
		// Instanciation et configuration d'un client REST.
		RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/scan", this);
		client.AddParam("format", scanFormat);
		client.AddParam("content", scanContent.replace(" ", ""));
		client.setRequestType(RequestMethod.GET);
		try {
			client.execute(); // Execution de la requete de scan
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode permettant de convertir une array de byte en un string hexadecimal
	 * @param  data  le byte[] a convertir
	 * @return String Le resultat converti
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
	 * Methode permettant de convertir un byte en string hexadecimal
	 * @param  data  Le byte a convertir
	 * @return String Le resultat converti
	 */
	private String byteToHex(byte data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	/**
	 * Methode permettant de convertir un int en char
	 * @param  i  l'int a convertir
	 * @return char le char resultant
	 */
	private char toHexChar(int i) {
		if ((0 <= i) && (i <= 9)) {
			return (char) ('0' + i);
		} else {
			return (char) ('a' + (i - 10));
		}
	}    

	/**
	 * Listener
	 */
	@Override
	public void onClick(View arg0) {
		if(arg0.getId()==R.id.scan_button){ // Cas du bouton commandant un scan de code-barres
			state = 1; // La derniere action est un scan
			
			// On passe la main a Zxing pour le scan de code-barres.
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}
		if(arg0.getId()==R.id.scan_status){ // Cas du bouton commandant la mise a jour de statut
			state = 2; // La derniere action est une mise a jour de statut
			
			// On recupere les coordonnees du smartphone.
			CurrentLocation location = new CurrentLocation(this);

			// Instanciation et configuration d'un client REST.
			RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/update", this);
			client.AddParam("format", this.scanFormat);
			client.AddParam("content", this.scanContent.replace(" ", ""));
			client.AddParam("latitude", location.getLatitude());
			client.AddParam("longitude", location.getLongitude());
			client.setRequestType(RequestMethod.GET);
			try {
				client.execute(); // Execution de la requete de mise a jour de statut
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Methode appelee automatiquement lorsque Zxing rend la main a notre application.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//On recupere les resultats du scan de code-barres.
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			this.scanContent = scanningResult.getContents();
			this.scanFormat = scanningResult.getFormatName();
			
			// Et on les affiche.
			this.formatTxt.setText("FORMAT: " + scanFormat);
			this.contentTxt.setText("CONTENT: " + scanContent.replace(" ", ""));
			this.responseTxt.setText("");

			// Instanciation et configuration d'un client REST.
			RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/scan", this);
			client.AddParam("format", scanFormat);
			client.AddParam("content", scanContent);
			client.setRequestType(RequestMethod.GET);
			try {
				client.execute(); // Execution de la requete de scan
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

	/**
	 * Methode permettant au client REST de notifier l'activite une fois la reponse a la requete arrivee.
	 * @see rest.RestClient#execute(Void...)
	 * @param response La reponse a notre requete HTTP
	 */
	public void notifyResult(final String response) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				if (state == 2){ // La derniere activite est une mise a jour de statut.
					statusBtn.setVisibility(View.INVISIBLE); // Il faut donc desactiver le bouton de mise a jour de statut.
					
					//Afficher le nouveau statut.
					Toast toast = Toast.makeText(getApplicationContext(),
							"New status is " + response, Toast.LENGTH_LONG);
					toast.show();
					
					//On affiche le nouveau statut en dessous des informations sur le colis.
					Scanner scan = new Scanner(responseTxt.getText().toString());
					scan.useDelimiter("\n");
					String result = scan.next(); // La premiere ligne contient les infos du colis, qu'il faut conserver.
					result += "\n Current status: " + response; // La deuxieme ligne contient le statut courant, qu'il faut mettre a jour.
					responseTxt.setText(result);
					scan.close();
				}
				else{ // State = 1, la derniere activite est un scan de colis.
					// On affiche les infos du colis et on rend visible le bouton de mise a jour de statut.
					responseTxt.setText(response);
					statusBtn.setVisibility(View.VISIBLE);
				}
			}
		});	
	}
}
