package projet.cnam.teleconsultmobile.Tasks;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * SubmitResult
 * =============
 * Demande la création d'un resultat (Dossier) au Webservice du système Teleconsult
 * @author TeleconsultTeam
 * @licence BSD
 */
public class SubmitResult extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        String consultID = params[0];
        String examenID = params[1];
        String imageName = params[2];
        String imagePath = params[3];
        String heartString = params[4];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<String> response = client.get("/createResult")
                .param("consultID", consultID)
                .param("examenID", examenID)
                .param("imageName", imageName+".jpg")
                .param("imagePath", imagePath)
                .param("heartEntry", heartString)
                .asString();
        return null;
    }
}
