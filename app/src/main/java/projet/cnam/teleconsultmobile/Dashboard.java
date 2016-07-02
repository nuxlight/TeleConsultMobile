package projet.cnam.teleconsultmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.cnam.teleconsultmobile.Tasks.ListnerMedicInfoTask;
import projet.cnam.teleconsultmobile.Tasks.MedicInfoTask;

import static projet.cnam.teleconsultmobile.R.drawable.femdoc;

public class Dashboard extends AppCompatActivity implements ListnerMedicInfoTask{

    private TextView welcomeLabel;
    private ImageView photo;
    private TextView addrLabel;
    private TextView speLabel;
    private TextView folderStatus;
    private ListView folderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle bundle = getIntent().getExtras();
        String[] username = new String[]{bundle.getString("user")};

        //Get all widgets
        welcomeLabel = (TextView) findViewById(R.id.welcomeLabelID);
        addrLabel = (TextView) findViewById(R.id.addrID);
        speLabel = (TextView) findViewById(R.id.specialiteID);
        photo = (ImageView) findViewById(R.id.photoID);
        folderStatus = (TextView) findViewById(R.id.folderStatusID);
        folderList = (ListView) findViewById(R.id.folderListID);

        //Get doctor information from webservice
        MedicInfoTask medicInfoTask = new MedicInfoTask(Dashboard.this);
        medicInfoTask.execute(username);
    }

    @Override
    public void onMedicInformationResult(JSONArray object) throws JSONException {
        JSONObject jsonObject = object.getJSONObject(0);
        //Modify Dashboard widgets with doctor information
        welcomeLabel.setText("Bonjour docteur "+jsonObject.get("name"));
        if (jsonObject.get("genre").equals("F")){
            photo.setImageResource(R.drawable.femdoc);
        }
        else {
            photo.setImageResource(R.drawable.mendoc);
        }
        addrLabel.setText(jsonObject.get("adresse").toString());
        speLabel.setText(jsonObject.get("specialite").toString());
    }
}
