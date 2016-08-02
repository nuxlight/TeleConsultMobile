package projet.cnam.teleconsultmobile;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity{

    private EditTextPreference serverPref;
    private EditTextPreference serverPortPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        serverPref = (EditTextPreference)findPreference("pref_server_address");
        serverPref.setText(appPreference.SERVER_ADDR);
        serverPref.setSummary(serverPref.getText());

        serverPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                appPreference.SERVER_PORT = newValue.toString();
                return false;
            }
        });

        serverPortPref = (EditTextPreference)findPreference("pref_server_port");
        serverPortPref.setText(appPreference.SERVER_PORT);
        serverPortPref.setSummary(serverPortPref.getText());
    }
}
