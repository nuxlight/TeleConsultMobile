package projet.cnam.teleconsultmobile.Tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.goebl.david.Request;
import com.goebl.david.Response;
import com.goebl.david.Webb;

import java.net.HttpURLConnection;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * Login on webservice Teleconsult
 * @author Thibaud Pellissier
 * @version 0.1
 */
public class LoginTask extends AsyncTask<String, Void, Boolean>{

    private ListenerLoginTask listenerLoginTask;

    public LoginTask(ListenerLoginTask listenerLoginTask){
        this.listenerLoginTask = listenerLoginTask;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String username = strings[0];
        String password = strings[1];
        String urlLogin = "http://"+appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";
        HttpURLConnection connection = null;
        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<String> response = client.get("/auth")
                .param("name", username)
                .param("password", password)
                .asString();
        Log.d(getClass().getName(), response.getBody());
        if (response.getBody().equals("true")){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean){
            listenerLoginTask.onLoginTaskTrue();
        }
        else {
            listenerLoginTask.onLoginTaskFalse();
        }
        super.onPostExecute(aBoolean);
    }
}
