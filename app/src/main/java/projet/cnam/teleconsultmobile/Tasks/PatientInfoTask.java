package projet.cnam.teleconsultmobile.Tasks;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * PatientInfoTask
 * =============
 * Récupération des informations des patients liés au médecin connecté
 * @author TeleconsultTeam
 * @licence BSD
 */
public class PatientInfoTask extends AsyncTask<String, Void, JSONArray> {

    private ListenerPatientInfoTask listenerPatientInfoTask;

    public PatientInfoTask(ListenerPatientInfoTask listener){
        this.listenerPatientInfoTask = listener;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {
        String medicID = strings[2];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<JSONArray> response = client.get("/getPatients")
                .param("medicID", medicID)
                .asJsonArray();
        return response.getBody();
    }

    @Override
    protected void onPostExecute(JSONArray jsonObject) {
        try {
            this.listenerPatientInfoTask.onPatientInformationResult(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonObject);
    }
}
