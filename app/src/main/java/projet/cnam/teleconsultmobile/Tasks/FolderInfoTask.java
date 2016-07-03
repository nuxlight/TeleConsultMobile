package projet.cnam.teleconsultmobile.Tasks;

import android.os.AsyncTask;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * Created by thibaud on 02/07/16.
 */
public class FolderInfoTask extends AsyncTask<String, Void, JSONArray> {

    private ListnerFolderInfoTask listnerFolderInfoTask;

    public FolderInfoTask(ListnerFolderInfoTask listener){
        this.listnerFolderInfoTask = listener;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {
        String username = strings[0];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<JSONArray> response = client.get("/listFolder")
                .param("medic", username)
                .asJsonArray();
        return response.getBody();
    }

    @Override
    protected void onPostExecute(JSONArray jsonObject) {
        try {
            this.listnerFolderInfoTask.onListnerFolderInfoTaskResult(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonObject);
    }
}
