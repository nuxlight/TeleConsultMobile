package projet.cnam.teleconsultmobile.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import projet.cnam.teleconsultmobile.R;
import projet.cnam.teleconsultmobile.Tasks.ListenerLoginTask;
import projet.cnam.teleconsultmobile.Tasks.LoginTask;
import projet.cnam.teleconsultmobile.appPreference;

public class MainActivity extends Activity implements ListenerLoginTask {

    private EditText usernameEntry;
    private EditText passwordEntry;
    private Button  loginBtn;
    private Button  configBtn;
    private projet.cnam.teleconsultmobile.appPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize pref data
        appPreference = new appPreference(getApplicationContext());

        //Get all graphical part
        usernameEntry = (EditText) findViewById(R.id.login_username_entry);
        passwordEntry = (EditText) findViewById(R.id.login_pass_entry);
        loginBtn = (Button) findViewById(R.id.login_button);
        configBtn = (Button) findViewById(R.id.config_button);

        //Check preferences
        String[] prefs = appPreference.getUserPrefs();
        usernameEntry.setText(prefs[0]);
        passwordEntry.setText(prefs[1]);

        //Login in app
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] perfsResult = new String[]{usernameEntry.getText().toString(),passwordEntry.getText().toString()};
                LoginTask loginTask = new LoginTask(MainActivity.this);
                loginTask.execute(perfsResult);
            }
        });

        //Start the configuration activity
        configBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSet = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSet);
            }
        });
    }

    @Override
    public void onLoginTaskTrue(String auth) {
        //Save users preferences
        this.appPreference.saveUserPrefs(usernameEntry.getText().toString(), passwordEntry.getText().toString());
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        intent.putExtra("medicID", auth);
        intent.putExtra("user", usernameEntry.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onLoginTaskFalse() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setPositiveButton("ok", null);
        dialog.setMessage("Erreur de connexion");
        dialog.show();
    }
}
