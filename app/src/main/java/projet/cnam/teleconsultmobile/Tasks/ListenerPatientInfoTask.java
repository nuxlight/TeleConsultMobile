package projet.cnam.teleconsultmobile.Tasks;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by thibaud on 24/08/16.
 */
public interface ListenerPatientInfoTask {
    void onPatientInformationResult(JSONArray object) throws JSONException;
}
