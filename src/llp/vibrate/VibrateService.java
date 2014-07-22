package llp.vibrate;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;

public class VibrateService extends Service {

    private Vibrator vibrator;
    private boolean isChange;
    
    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    public Vibrator getVibrator() {
        return vibrator;
    }

    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public boolean isOn;
    private long[] pattern;
    private final Binder vibrateServiveBinder = new ServiceBinder();

    public void setPattern(long[] pattern) {
        this.pattern = pattern;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return vibrateServiveBinder;
    }

    public class ServiceBinder extends Binder{
        public VibrateService getService() {
            return VibrateService.this;
        }
    }

    public boolean startVibrate() {
        if (!isOn || isChange) {
            vibrator.vibrate(pattern, 0);
            isOn = true;
        } else {
            vibrator.cancel();
            isOn = false;
        }    
        return isOn;
    }
    
}
