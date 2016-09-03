package projet.cnam.teleconsultmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by thibaud on 02/07/16.
 */
public class appPreference {

    //All variables configurable
    //public static String SERVER_ADDR = "10.0.2.2";
    public static String SERVER_ADDR = "teleconsultweb.ovh";
    public static String SERVER_PORT = "7777";
    public static String username_medic = "";
    public static String password_medic = "";
    public Context appContext;

    public appPreference(Context context) {
        appContext = context;
    }

    public void saveUserPrefs(String compte_id, String code_auditeur, String id_medic) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username_medic", compte_id);
        editor.putString("password_medic", code_auditeur);
        editor.putString("id_medic", id_medic);
        editor.commit();
    }

    public String[] getUserPrefs(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        String[] perfsResult = new String[]{preferences.getString("username_medic", "null"), preferences.getString("password_medic", "null"), preferences.getString("id_medic", "null")};
        Log.i(getClass().getSimpleName(), "getUserPrefs = "+
                preferences.getString("username_medic", "null")+" - "+
                preferences.getString("password_medic", "null"));
        return perfsResult;
    }

    public Context getAppContext(){
        return appContext;
    }
}
