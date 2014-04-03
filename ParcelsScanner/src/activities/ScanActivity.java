package activities;

import com.example.parcelsscanner.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class ScanActivity extends Activity {

	Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
	}

}
