package projet.cnam.teleconsultmobile.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import projet.cnam.teleconsultmobile.appPreference;

/**
 * SubmitConsult
 * =============
 * Demande la création d'une consultation au Webservice du système Teleconsult
 * @author TeleconsultTeam
 * @licence BSD
 */
public class SubmitConsult extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String patientID = params[0];
        String traitement = params[1];
        String historique = params[2];
        String urlLogin = "http://"+ appPreference.SERVER_ADDR+":"
                +appPreference.SERVER_PORT+"/";

        //Create date of the day
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        Log.d(getClass().getName(),"DATE : "+formatted);
        Webb client = Webb.create();
        client.setBaseUri(urlLogin);
        Response<String> response = client.get("/createConsult")
                .param("patientID", patientID)
                .param("date", formatted)
                .param("traitement", traitement)
                .param("histo", historique)
                .asString();
        return null;
    }
}
