package projet.cnam.teleconsultmobile.Tasks;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by thibaud on 02/07/16.
 */
public interface ListnerFolderInfoTask {
    void onListnerFolderInfoTaskResult(JSONArray object) throws JSONException;
}
