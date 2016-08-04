package projet.cnam.teleconsultmobile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import projet.cnam.teleconsultmobile.handler.MessengerEnvoiFichiersHandler;
import projet.cnam.teleconsultmobile.handler.ServiceActivityListener;
import projet.cnam.teleconsultmobile.services.ServiceEnvoiFichiers;

public class ServiceTestActivity extends AppCompatActivity implements ServiceActivityListener{
    private static final String TAG = "" + ServiceTestActivity.class.getSimpleName();
    /**
     * Messenger pour envoyer des messages au service
     */
    private Messenger mService = null;
    /**
     * Messenger transmis au service pour envoyer des messages à l'activité
     */
    private final Messenger mMessenger = new Messenger(new MessengerEnvoiFichiersHandler(this));

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;
            envoyerMessageAuService(ServiceEnvoiFichiers.MSG_SAY_HELLO,null, false);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };

    /**
     * Méthode générique d'envoi d'un message au service Android
     * @param type  type de message
     * @param object    objet (PaquetComm, SauvegardeFichierBean, etc.)
     * @param envReplyTo (boolean) Envoyer messager client pour pouvoir messages du service Android
     */
    public void envoyerMessageAuService(int type, Object object, boolean envReplyTo) {
        try {
            Message msg = Message.obtain(null, type);

            if (object != null) {
                msg.obj = object;
            }

            if (envReplyTo) {
                msg.replyTo = mMessenger;
            }
            mService.send(msg);

        } catch (RemoteException e) {
            Log.e(TAG, "Impossible d'envoyer le message au service, " +
                    "verifier qu'il a bien été instancié", e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        Log.e(TAG, "CREATE");
        Intent intent = new Intent(this, ServiceEnvoiFichiers.class);
        startService(intent);
        Log.e(TAG, "Start service");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "START");
        // Bind to LocalService
        Intent intent = new Intent(ServiceTestActivity.this, ServiceEnvoiFichiers.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "Bind don");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mConnection=null;
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mConnection=null;
            mBound = false;
        }
    }

}
