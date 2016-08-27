package projet.cnam.teleconsultmobile.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * Created by thibaud on 26/08/16.
 * Envoi d'une consultation au WebService
 */
public class SubmitConsult extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String patientID = params[0];
        String traitement = params[1];
        String historique = params[2];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<String> response = client.get("/createConsult")
                .param("patientID", patientID)
                .param("traitement", traitement)
                .param("histo", historique)
                .asString();
        return null;
    }
}
