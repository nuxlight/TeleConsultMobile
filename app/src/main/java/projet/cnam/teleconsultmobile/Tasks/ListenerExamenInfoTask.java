package projet.cnam.teleconsultmobile.Tasks;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by thibaud on 27/08/16.
 */
public interface ListenerExamenInfoTask {
    void onExamenInformationResult(JSONArray object) throws JSONException;
}
