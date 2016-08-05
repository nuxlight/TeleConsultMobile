package projet.cnam.teleconsultmobile.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import projet.cnam.teleconsultmobile.services.ServiceEnvoiFichiers;

/**
 * Created by nikolai on 12/07/16.
 */
public class MessengerEnvoiFichiersHandler extends Handler{
    private static final String TAG = "" + MessengerEnvoiFichiersHandler.class.getSimpleName();
    private ServiceActivityListener listener;

    public MessengerEnvoiFichiersHandler(ServiceActivityListener listener) {
        this.listener = listener;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ServiceEnvoiFichiers.ENVOI_IMAGE:
                Log.e(TAG, "HANDLE MESSAGE FROM SERVICE");
                Log.e(TAG, "Contenu message = " + msg.obj);
                break;
        }
    }
}
