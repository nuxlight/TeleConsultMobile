package projet.cnam.teleconsultmobile.Activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import projet.cnam.teleconsultmobile.R;
import projet.cnam.teleconsultmobile.Tasks.ConsultInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ExamenInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListenerExamenInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListenerPatientInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListnerConsultInfoTask;
import projet.cnam.teleconsultmobile.Tasks.PatientInfoTask;
import projet.cnam.teleconsultmobile.Tasks.SubmitExamen;
import projet.cnam.teleconsultmobile.Tasks.SubmitResult;
import projet.cnam.teleconsultmobile.appPreference;
import projet.cnam.teleconsultmobile.handler.MessengerEnvoiFichiersHandler;
import projet.cnam.teleconsultmobile.handler.ServiceActivityListener;
import projet.cnam.teleconsultmobile.services.ServiceEnvoiFichiers;

public class ResultActivity extends AppCompatActivity implements ListenerPatientInfoTask, ListnerConsultInfoTask, ListenerExamenInfoTask, ServiceActivityListener {

    //UI elements
    private TextView consultView;
    private Button consultBtn;
    private TextView examenView;
    private Button examenBtn;
    private TextView photoView;
    private Button photoBtn;
    private Button resultSendBtn;
    private Context appContext;
    private ImageView photoViewImg;
    //others var
    private String patientID = "";
    private String consultID = "";
    private String examenID = "";
    private String imageName = "";
    private String imagePath = "";
    private String[] medicInfo;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //Service variable
    private Messenger mService = null;
    private final Messenger mMessenger = new Messenger(new MessengerEnvoiFichiersHandler(this));
    boolean mBound;

    //Photo intent return
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageName = medicInfo[2]+"-"+consultID+"-"+examenID+"-img";
            Bundle extras = data.getExtras();
            String path = getApplicationContext().getCacheDir().toString();
            OutputStream fOut = null;
            File file = new File(path, imageName+".jpg"); // the File to save to
            try {
                fOut = new FileOutputStream(file);
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close(); // do not forget to close the stream
                photoViewImg.setImageBitmap(imageBitmap);
                photoView.setText("Image : "+imageName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mBound = true;
            envoyerMessageAuService(ServiceEnvoiFichiers.ENVOI_IMAGE,null, false);
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService

    }

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
            Log.e(getClass().getName(), "Impossible d'envoyer le message au service, " +
                    "verifier qu'il a bien été instancié", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.resultat_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Resultats</font>"));
        //For dialog context
        appContext = getApplicationContext();
        appPreference appPreference = new appPreference(appContext);
        medicInfo = appPreference.getUserPrefs();

        //Starting image send service
        final Intent intent = new Intent(this, ServiceEnvoiFichiers.class);
        startService(intent);

        //Get all UI elements
        consultView = (TextView) findViewById(R.id.viewResultConsult);
        consultBtn = (Button) findViewById(R.id.btnResultConsult);
        examenView = (TextView) findViewById(R.id.viewResultExamen);
        examenBtn = (Button) findViewById(R.id.btnResultExamen);
        photoView = (TextView) findViewById(R.id.viewResultImg);
        photoBtn = (Button) findViewById(R.id.btnResultImg);
        photoViewImg = (ImageView) findViewById(R.id.resultImg);
        resultSendBtn = (Button) findViewById(R.id.btnResultSend);

        //Configure button actions
        consultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientInfoTask patientInfoTask = new PatientInfoTask(ResultActivity.this);
                appPreference appPreference = new appPreference(appContext);
                String[] medicInfo = appPreference.getUserPrefs();
                patientInfoTask.execute(medicInfo);
            }
        });

        examenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamenInfoTask examenInfoTask = new ExamenInfoTask(ResultActivity.this);
                examenInfoTask.execute(medicInfo);
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        //Validate button
        resultSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!consultID.equals("") && !examenID.equals("") && !imageName.equals("")){
                    imagePath = "./images/"+imageName+".jpg";
                    String[] strings = {consultID, examenID, imageName, imagePath};
                    //SubmitResult submitResult = new SubmitResult();
                    //submitResult.execute(strings);
                    Toast.makeText(ResultActivity.this,"Resultat rajouté", Toast.LENGTH_SHORT).show();
                    //Connection to send service
                    sendImageToServer(imageName);
                    //Quit activity
                    finish();
                    stopService(intent);
                }
                else {
                    Toast.makeText(ResultActivity.this,"Merci de completer les cases", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendImageToServer(String imageName) {
        Intent intent = new Intent(ResultActivity.this, ServiceEnvoiFichiers.class);
        Log.d(getClass().getName(), "Binding image : "+imageName);
        intent.putExtra("image_name", imageName);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPatientInformationResult(JSONArray object) throws JSONException {
        AlertDialog.Builder consultBuilder = new AlertDialog.Builder(ResultActivity.this);
        consultBuilder.setTitle("Patient :");
        ArrayList<String> tempList = new ArrayList<String>();
        for (int a=0;a<object.length();a++){
            JSONObject tempObjet = object.getJSONObject(a);
            tempList.add(tempObjet.getString("patient_name")+"/"+tempObjet.getString("patient_id"));
        }
        final String[] cs = tempList.toArray(new String[tempList.size()]);
        consultBuilder.setItems(cs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] choiceID = cs[which].split("/");
                patientID = choiceID[1];
                ConsultInfoTask consultInfoTask = new ConsultInfoTask(ResultActivity.this);
                consultInfoTask.execute(choiceID);
            }
        });
        consultBuilder.create().show();
    }

    @Override
    public void onConsultInfoResult(JSONArray object) throws JSONException {
        AlertDialog.Builder consultBuilder = new AlertDialog.Builder(ResultActivity.this);
        consultBuilder.setTitle("Consultation :");
        ArrayList<String> tempList = new ArrayList<String>();
        for (int a=0;a<object.length();a++){
            JSONObject tempObjet = object.getJSONObject(a);
            tempList.add(tempObjet.getString("consult_id"));
        }
        final String[] cs = tempList.toArray(new String[tempList.size()]);
        consultBuilder.setItems(cs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consultID = cs[which];
                consultView.setText("Consultation : "+consultID);
                imageName = medicInfo[2]+"-"+consultID+"-"+examenID+"-img";
                photoView.setText("Image : "+imageName);
            }
        });
        consultBuilder.create().show();
    }

    @Override
    public void onExamenInformationResult(JSONArray object) throws JSONException {
        AlertDialog.Builder consultBuilder = new AlertDialog.Builder(ResultActivity.this);
        consultBuilder.setTitle("Examen :");
        ArrayList<String> tempList = new ArrayList<String>();
        for (int a=0;a<object.length();a++){
            JSONObject tempObjet = object.getJSONObject(a);
            tempList.add(tempObjet.getString("examen_id")+"/"+tempObjet.getString("examen_nom"));
        }
        final String[] cs = tempList.toArray(new String[tempList.size()]);
        consultBuilder.setItems(cs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] choiceID = cs[which].split("/");
                examenID = choiceID[0];
                examenView.setText("Examen : "+cs[which]);
                imageName = medicInfo[2]+"-"+consultID+"-"+examenID+"-img";
                photoView.setText("Image : "+imageName);
            }
        });
        consultBuilder.create().show();
    }
}
