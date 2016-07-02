package projet.cnam.teleconsultmobile.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * MedicInfoTask
 * =============
 * @author Thibaud
 * @version 0.1
 */
public class MedicInfoTask extends AsyncTask<String, Void, JSONArray> {

    private ListnerMedicInfoTask listnerMedicInfoTask;

    public MedicInfoTask(ListnerMedicInfoTask listener){
        this.listnerMedicInfoTask = listener;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {
        String username = strings[0];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<JSONArray> response = client.get("/getMedicInfo")
                .param("name", username)
                .asJsonArray();
        return response.getBody();
    }

    @Override
    protected void onPostExecute(JSONArray jsonObject) {
        try {
            this.listnerMedicInfoTask.onMedicInformationResult(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonObject);
    }
}
