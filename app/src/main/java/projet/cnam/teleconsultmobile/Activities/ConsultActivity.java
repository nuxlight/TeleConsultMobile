package projet.cnam.teleconsultmobile.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import projet.cnam.teleconsultmobile.R;
import projet.cnam.teleconsultmobile.Tasks.ListenerPatientInfoTask;
import projet.cnam.teleconsultmobile.Tasks.PatientInfoTask;
import projet.cnam.teleconsultmobile.Tasks.SubmitConsult;
import projet.cnam.teleconsultmobile.appPreference;

public class ConsultActivity extends AppCompatActivity implements ListenerPatientInfoTask {

    private EditText patientTrait;
    private EditText patientHisto;
    private Spinner patientSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        Toolbar toolbar = (Toolbar) findViewById(R.id.consult_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Consultation</font>"));

        patientSelector = (Spinner) findViewById(R.id.patient_selector);
        patientTrait = (EditText) findViewById(R.id.patient_traitement);
        patientHisto = (EditText) findViewById(R.id.patient_histo);
        Button sendButton = (Button) findViewById(R.id.btn_consult);

        //populate the spinner patient
        appPreference appPreference = new appPreference(ConsultActivity.this);
        String[] medicInfo = appPreference.getUserPrefs();
        PatientInfoTask patientInfoTask = new PatientInfoTask(ConsultActivity.this);
        patientInfoTask.execute(medicInfo);

        //Send a new consultation
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!patientTrait.getText().toString().equals("") && !patientHisto.getText().toString().equals("")){
                    SubmitConsult submitConsult = new SubmitConsult();
                    //Forge parameter to send to Webservice
                    String[] patientParms = patientSelector.getSelectedItem().toString().split("/");
                    ArrayList<String> parms = new ArrayList<String>();
                    parms.add(patientParms[1]);
                    parms.add(patientTrait.getText().toString());
                    parms.add(patientHisto.getText().toString());
                    String[] strings = parms.toArray(new String[parms.size()]);
                    submitConsult.execute(strings);
                    Toast.makeText(ConsultActivity.this,"Consultation rajoutée", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(ConsultActivity.this,"Merci de remplir les cases", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onPatientInformationResult(JSONArray object) throws JSONException {
        //save patients infos
        ArrayList<String> tempList = new ArrayList<String>();
        for (int a=0; a < object.length(); a++){
            JSONObject tempObjet = object.getJSONObject(a);
            tempList.add(tempObjet.getString("patient_name")+"/"+tempObjet.getString("patient_id"));
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, tempList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.patientSelector.setAdapter(spinnerArrayAdapter);
    }
}
