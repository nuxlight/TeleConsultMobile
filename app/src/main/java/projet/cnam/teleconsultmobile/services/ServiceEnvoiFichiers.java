package projet.cnam.teleconsultmobile.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import projet.cnam.teleconsultmobile.runnable.HttpReq;

public class ServiceEnvoiFichiers extends Service {
    private static final String TAG = "" + ServiceEnvoiFichiers.class.getSimpleName();
    /** Command to the service to display a message */
    public static final int ENVOI_IMAGE = 1;
    public static final int IMAGE_ENVOYE = 2;
    public static final int ERROR = -9;

    /**
     * Handler http
     */
    private final Handler httpHandler = new HttpHandler();
    class HttpHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IMAGE_ENVOYE:
                    Toast.makeText(getApplicationContext(), "Image envoyé!", Toast.LENGTH_LONG).show();
                    Message message = Message.obtain();
                    message.what = msg.what;
                    message.obj = 3;
                    Log.d(getClass().getName(), "DEBUG : "+msg);
                    envoyerMessageClient(message);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    private Messenger clientMessenger;
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg != null && msg.replyTo != null){
                clientMessenger = msg.replyTo;
            }
            Thread thread;
            switch (msg.what) {
                case ENVOI_IMAGE:
                    try {
                        InputStream ims = getAssets().open("femdoc.png");
                        HttpReq postDonnees = new HttpReq(httpHandler,ims);
                        thread = new Thread(postDonnees);
                        thread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "ON CREATE SERVICE");
    }



    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void envoyerMessageClient(Message message){
        try {
            clientMessenger.send(message);
        } catch (Exception e) {
            // The client is dead.  Remove it from the list;
            Log.e(TAG, "Client NULL :" + e.toString(), e);
            Toast.makeText(getApplicationContext(), "Aucun client!", Toast.LENGTH_LONG).show();

        }
    }
}
