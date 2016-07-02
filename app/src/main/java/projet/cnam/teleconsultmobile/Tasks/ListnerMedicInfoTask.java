package projet.cnam.teleconsultmobile.Tasks;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by thibaud on 02/07/16.
 */
public interface ListnerMedicInfoTask {
    void onMedicInformationResult(JSONArray object) throws JSONException;
}
