package projet.cnam.teleconsultmobile.Tasks;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * Created by thibaud on 27/08/16.
 */
public class ExamenInfoTask extends AsyncTask<String, Void, JSONArray> {

    private ListenerExamenInfoTask listenerExamenInfoTask;

    public ExamenInfoTask(ListenerExamenInfoTask listenerExamenInfoTask) {
        this.listenerExamenInfoTask = listenerExamenInfoTask;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        //String username = params[0];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<JSONArray> response = client.get("/getExams")
                .param("medicID", '1')
                .asJsonArray();
        return response.getBody();
    }

    @Override
    protected void onPostExecute(JSONArray jsonObject) {
        try {
            this.listenerExamenInfoTask.onExamenInformationResult(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonObject);
    }
}
