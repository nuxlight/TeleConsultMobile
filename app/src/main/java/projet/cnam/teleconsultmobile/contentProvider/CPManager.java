package projet.cnam.teleconsultmobile.contentProvider;

import android.content.ContentResolver;

import java.util.List;

//import projet.cnam.teleconsultmobile.Folder;

/**
 * Created by nikolai on 20/07/16.
 */
public class CPManager {

    private ContentResolver contentResolver;

    public CPManager(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

   /* public List<Folder> getFolders(){
     return null;
    }*/
}
