package projet.cnam.teleconsultmobile.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import projet.cnam.teleconsultmobile.R;
import projet.cnam.teleconsultmobile.Tasks.ConsultInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ExamenInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListenerExamenInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListenerPatientInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListnerConsultInfoTask;
import projet.cnam.teleconsultmobile.Tasks.PatientInfoTask;

public class ResultActivity extends AppCompatActivity implements ListenerPatientInfoTask, ListnerConsultInfoTask, ListenerExamenInfoTask {

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
    private String patientID;
    private String consultID;
    private String examenID;
    private String imageName;
    private String imagePath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //Photo intent return
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoViewImg.setImageBitmap(imageBitmap);
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
                patientInfoTask.execute();
            }
        });

        examenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamenInfoTask examenInfoTask = new ExamenInfoTask(ResultActivity.this);
                examenInfoTask.execute();
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
            }
        });
        consultBuilder.create().show();
    }
}
