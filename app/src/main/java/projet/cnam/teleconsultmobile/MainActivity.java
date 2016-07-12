package projet.cnam.teleconsultmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import projet.cnam.teleconsultmobile.Tasks.ListenerLoginTask;
import projet.cnam.teleconsultmobile.Tasks.LoginTask;

public class MainActivity extends Activity implements ListenerLoginTask {

    private EditText usernameEntry;
    private EditText passwordEntry;
    private Button  loginBtn;
    private appPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize pref data
        appPreference = new appPreference(MainActivity.this);

        //Get all graphical part
        usernameEntry = (EditText) findViewById(R.id.login_username_entry);
        passwordEntry = (EditText) findViewById(R.id.login_pass_entry);
        loginBtn = (Button) findViewById(R.id.login_button);

        //Check preferences
        SharedPreferences preferences = getSharedPreferences(MainActivity.class.getSimpleName(), 1);
        usernameEntry.setText(preferences.getString("username_medic", ""));
        passwordEntry.setText(preferences.getString("password_medic", ""));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] perfsResult = new String[]{usernameEntry.getText().toString(),passwordEntry.getText().toString()};
                LoginTask loginTask = new LoginTask(MainActivity.this);
                loginTask.execute(perfsResult);
            }
        });
    }

    @Override
    public void onLoginTaskTrue() {
        //Save users preferences
        this.appPreference.saveUserPrefs(usernameEntry.getText().toString(), passwordEntry.getText().toString());
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
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
