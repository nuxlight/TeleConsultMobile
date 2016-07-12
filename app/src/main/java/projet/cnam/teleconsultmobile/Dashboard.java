package projet.cnam.teleconsultmobile;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import projet.cnam.teleconsultmobile.Adaptors.FoldersAdaptor;
import projet.cnam.teleconsultmobile.Tasks.FolderInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListnerFolderInfoTask;
import projet.cnam.teleconsultmobile.Tasks.ListnerMedicInfoTask;
import projet.cnam.teleconsultmobile.Tasks.MedicInfoTask;

import static projet.cnam.teleconsultmobile.R.drawable.femdoc;

public class Dashboard extends AppCompatActivity implements ListnerMedicInfoTask, ListnerFolderInfoTask{

    private TextView welcomeLabel;
    private ImageView photo;
    private TextView addrLabel;
    private TextView speLabel;
    private TextView folderStatus;
    private ListView folderList;
    private String[] username;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_menu).color(Color.WHITE).sizeDp(32));

        Bundle bundle = getIntent().getExtras();
        this.username = new String[]{bundle.getString("user")};

        //Create the NavigationDrawer
        generateDrawer();

        //Get all widgets
        welcomeLabel = (TextView) findViewById(R.id.welcomeLabelID);
        addrLabel = (TextView) findViewById(R.id.addrID);
        speLabel = (TextView) findViewById(R.id.specialiteID);
        photo = (ImageView) findViewById(R.id.photoID);
        folderStatus = (TextView) findViewById(R.id.folderStatusID);
        folderList = (ListView) findViewById(R.id.folderListID);

        //Get doctor information from webservice
        MedicInfoTask medicInfoTask = new MedicInfoTask(Dashboard.this);
        medicInfoTask.execute(this.username);
    }

    /**
     * This function generate all items in the drawer
     */
    private void generateDrawer(){
        //Make the header section (account section)
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.accountback)
                .addProfiles(
                        new ProfileDrawerItem().withName(this.username[0]).withIcon(FontAwesome.Icon.faw_user)
                )
                .build();
        //Create all items
        PrimaryDrawerItem DashItem = new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_home)
                .withName("Accueil").withIdentifier(1);
        PrimaryDrawerItem ConsultItem = new PrimaryDrawerItem()
                .withIcon(GoogleMaterial.Icon.gmd_add_box)
                .withName("Consultations").withIdentifier(2);
        PrimaryDrawerItem configItem = new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_build)
                .withName("Configuration").withIdentifier(3);
        //And you can create the final Drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .withAccountHeader(header)
                .addDrawerItems(
                        DashItem,
                        ConsultItem,
                        configItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 2:
                                Intent intentConsult = new Intent(Dashboard.this, ConsultActivity.class);
                                startActivity(intentConsult);
                            break;
                            case 3:
                                Intent intentSet = new Intent(Dashboard.this, SettingsActivity.class);
                                startActivity(intentSet);
                            break;
                        }
                        return false;
                    }
                })
                .build();
        drawer.setSelection(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMedicInformationResult(JSONArray object) throws JSONException {
        JSONObject jsonObject = object.getJSONObject(0);
        //Modify Dashboard widgets with doctor information
        welcomeLabel.setText("Bonjour docteur "+jsonObject.get("name"));
        if (jsonObject.get("genre").equals("F")){
            photo.setImageResource(R.drawable.femdoc);
        }
        else {
            photo.setImageResource(R.drawable.mendoc);
        }
        addrLabel.setText(jsonObject.get("adresse").toString());
        speLabel.setText(jsonObject.get("specialite").toString());
        //Get folder information with medic name (Task)
        FolderInfoTask folderInfoTask = new FolderInfoTask(Dashboard.this);
        folderInfoTask.execute(this.username);
    }

    @Override
    public void onListnerFolderInfoTaskResult(JSONArray object) throws JSONException {
        if (object.length()>0){
            folderStatus.setText("Vous avez "+object.length()+" dossier(s)");
        }
        //Create folders data sources and put the data in ArrayAdaptor to display information
        ArrayList<Folder> foldersList = new ArrayList<Folder>();
        for(int a=0;a<object.length();a++){
            JSONObject jsonFolder = object.getJSONObject(a);
            Folder folder = new Folder(jsonFolder.getString("patient"),
                    jsonFolder.getString("medecin"),
                    jsonFolder.getString("sexe"),
                    jsonFolder.getString("age"),
                    jsonFolder.getString("pathologie"),
                    jsonFolder.getString("avis_medecin"),
                    jsonFolder.getString("avis_ref"),
                    jsonFolder.getInt("etat_dossier"));
            foldersList.add(folder);
        }
        FoldersAdaptor foldersAdaptor = new FoldersAdaptor(this, 0, foldersList);
        this.folderList.setAdapter(foldersAdaptor);
    }
}
