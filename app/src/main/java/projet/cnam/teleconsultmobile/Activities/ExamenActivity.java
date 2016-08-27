package projet.cnam.teleconsultmobile.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import projet.cnam.teleconsultmobile.R;
import projet.cnam.teleconsultmobile.Tasks.ExamenInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListenerExamenInfoTask;
import projet.cnam.teleconsultmobile.Tasks.SubmitExamen;
import projet.cnam.teleconsultmobile.appPreference;

public class ExamenActivity extends AppCompatActivity implements ListenerExamenInfoTask {

    private EditText examenName;
    private Button button;
    private ListView listView;
    private String[] medicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examen_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Examens</font>"));

        examenName = (EditText) findViewById(R.id.examn_edit);
        button = (Button) findViewById(R.id.btnExamen);
        listView = (ListView) findViewById(R.id.list_examen);

        //Get examens list
        appPreference appPreference = new appPreference(ExamenActivity.this);
        medicInfo = appPreference.getUserPrefs();
        ExamenInfoTask examenInfoTask = new ExamenInfoTask(ExamenActivity.this);
        examenInfoTask.execute(medicInfo);

        //Adding examens
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!examenName.getText().toString().equals("")){
                    SubmitExamen submitExamen = new SubmitExamen();
                    String[] strings = {medicInfo[2], examenName.getText().toString()};
                    submitExamen.execute(strings);
                    ExamenInfoTask examenInfoTask = new ExamenInfoTask(ExamenActivity.this);
                    examenInfoTask.execute(medicInfo);
                }
                else {
                    Toast.makeText(ExamenActivity.this,"Merci de remplir les cases", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onExamenInformationResult(JSONArray object) throws JSONException {
        ArrayList<String> examList = new ArrayList<>();
        for (int a=0;a < object.length();a++){
            examList.add(object.getJSONObject(a).getString("examen_nom"));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examList);
        listView.setAdapter(arrayAdapter);
    }
}
