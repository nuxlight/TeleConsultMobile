package projet.cnam.teleconsultmobile.Tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.goebl.david.Request;
import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * LoginTask
 * =============
 * Connexion au Webservice de la solution Teleconsult
 * @author TeleconsultTeam
 * @licence BSD
 */
public class LoginTask extends AsyncTask<String, Void, JSONObject>{

    private ListenerLoginTask listenerLoginTask;

    public LoginTask(ListenerLoginTask listenerLoginTask){
        this.listenerLoginTask = listenerLoginTask;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String username = strings[0];
        String password = strings[1];
        String urlLogin = "http://"+appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";
        HttpURLConnection connection = null;
        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<JSONObject> response = client.get("/auth")
                .param("name", username)
                .param("password", password)
                .asJsonObject();
        return response.getBody();
    }

    @Override
    protected void onPostExecute(JSONObject jsonResult) {
        try {
            if (jsonResult.getString("auth").equals("true")){
                listenerLoginTask.onLoginTaskTrue(jsonResult.getString("medicID"));
            }
            else {
                listenerLoginTask.onLoginTaskFalse();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonResult);
    }
}
