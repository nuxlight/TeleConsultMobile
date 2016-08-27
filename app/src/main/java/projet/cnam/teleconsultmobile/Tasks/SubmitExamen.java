package projet.cnam.teleconsultmobile.Tasks;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * Created by thibaud on 27/08/16.
 */
public class SubmitExamen extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String medicID = params[0];
        String examenName = params[1];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<String> response = client.get("/createExamen")
                .param("medicID", medicID)
                .param("examenName", examenName)
                .asString();
        return null;
    }
}
