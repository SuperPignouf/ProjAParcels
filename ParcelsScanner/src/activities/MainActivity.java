/**
 * Premier ecran de l'application Android: gere le login et passe la main a l'activite de SCAN
 * en cas de login reussi.
 */

package activities;

import com.example.parcelsscanner.R;

import rest.RestClient;
import rest.RestClient.RequestMethod;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	/**
	 * Bouton de soumission de la requete de login.
	 */
	private Button submit = null;

	/**
	 * Champ de texte editable permettant de specifier le matricule.
	 */
	private EditText matricule = null;
	
	/**
	 * Champ de texte editable permettant de specifier le mot de passe.
	 */
	private EditText password = null;

	/**
	 * Une instance de "moi-meme", sera necessaire dans la definition d'un objet Runnable,
	 * ou "this" ne veut plus dire la meme chose.
	 * @see #notifyResult(String)
	 */
	private final MainActivity MA = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// On récupère toutes les vues dont on a besoin
		submit = (Button)findViewById(R.id.submit);
		matricule = (EditText)findViewById(R.id.matricule);
		password = (EditText)findViewById(R.id.password);

		// On attribue un listener au bouton de soumission
		submit.setOnClickListener(submitListener);

	}

	/**
	 * Listener du bouton "submit"
	 * @see #onCreate(Bundle)
	 */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
	        // On recupere le matricule et le mot de passe entres par l'utilisateur.
			String mat = matricule.getText().toString();
			String pass = password.getText().toString();
			if (mat.matches("") || pass.matches("")) // Envoi d'un message d'erreur si un champ est laisse vide.
				Toast.makeText(MA, "Please enter matricule and password", Toast.LENGTH_SHORT).show(); 
			else{	
				// Instanciation et configuration d'un client REST.
				RestClient client =  new RestClient("http://" + getString(R.string.restIP) + "/ParcelREST/rest/login", MA);
				client.AddParam("matricule", mat);
				client.AddParam("password", pass);
				client.setRequestType(RequestMethod.GET);
				try {
					client.execute(); // On commande au client d'executer la requete de login.
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * Methode permettant au client REST de notifier l'activite une fois la reponse a la requete arrivee.
	 * @see rest.RestClient#execute(Void...)
	 * @param response La reponse a notre requete HTTP
	 */
	public void notifyResult(String response) {
		if (response.contains("1")){ // Reponse positive: utilisateur valid.
			// On passe la main a l'activite de SCAN
			Intent intent = new Intent(getBaseContext(), ScanActivity.class);
			startActivity(intent);   
		}
		else if (response.contains("0")){ // Reponse negative: utilisateur invalide.
			this.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(MA, "Login failed !", Toast.LENGTH_SHORT).show(); // Message d'erreur
				}
			});
		}
	}
}