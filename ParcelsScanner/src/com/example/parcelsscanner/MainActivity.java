package com.example.parcelsscanner;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button submit = null;

	EditText matricule = null;
	EditText password = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
			// TODO: verification de l'identite aupres de la DB
		}
	};
}