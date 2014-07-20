package llp.vibrate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Vibrartion class
 * @author VoXuanHoan
 * @version $Id$
 */
public class MainActivity extends Activity {

	private Vibrator vibrator;
	private boolean isOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void click(View v) {
		Button button = (Button) findViewById(R.id.btClick);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgVibrateMode);

		long[] pattern;

		if (radioGroup.getCheckedRadioButtonId() == R.id.rbConstantly) {
			pattern = new long[]{0, 20};
		} else {
			pattern = new long[]{0, 200, 400, 800};
		}
		if (!isOn) {
			vibrator.vibrate(pattern, 0);
			button.setText(R.string.turn_off);
			isOn = true;
		} else {
			vibrator.cancel();
			button.setText(R.string.turn_on);
			isOn = false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

}
