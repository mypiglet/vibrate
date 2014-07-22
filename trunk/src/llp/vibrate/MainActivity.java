package llp.vibrate;

import llp.vibrate.VibrateService.ServiceBinder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Vibrartion class
 * 
 * @author VoXuanHoan
 * @version $Id$
 */
@SuppressLint("Wakelock")
public class MainActivity extends Activity {

    private VibrateService vibrateService;
    private boolean isBinded = false;
    private boolean isChangeMode;
    private Vibrator vibrator;

    // private static final String VIBRATE_SERVICE =
    // "llp.vibrate.VibrateService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgVibrateMode);
        rg.setOnCheckedChangeListener(rgClick);

        Intent vibrateIntent = new Intent(this, VibrateService.class);
        bindService(vibrateIntent, conectToVebrateService, Context.BIND_AUTO_CREATE);
        startService(vibrateIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "vibrateWakelock");
        wakeLock.acquire();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void click(View v) {
        Button button = (Button) findViewById(R.id.btClick);
        if (isBinded) {
            // set isCHange to false because click this button to stop vibrate
            if (vibrateService.isOn) isChangeMode = false;
            vibrateService.setChange(isChangeMode);
            createPattern();

            if (vibrateService.startVibrate()) {
                button.setText(R.string.turn_off);
            } else {
                button.setText(R.string.turn_on);
            }
        }
    }

    protected ServiceConnection conectToVebrateService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceBinder serviceBinder = (ServiceBinder) service;
            vibrateService = serviceBinder.getService();
            vibrateService.setVibrator(vibrator);
            createPattern();
            isBinded = true;
        }
    };

    private OnCheckedChangeListener rgClick = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            isChangeMode = vibrateService.isOn ? true : false;
            vibrateService.setChange(isChangeMode);
            createPattern();
            if (isChangeMode) {
                vibrateService.startVibrate();
            }
        }
    };

    private void createPattern() {
        long[] pattern;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgVibrateMode);
        if (radioGroup.getCheckedRadioButtonId() == R.id.rbConstantly) {
            pattern = new long[] { 0, 20 };
        } else {
            pattern = new long[] { 0, 200, 400, 800 };
        }

        vibrateService.setPattern(pattern);
    }
}
