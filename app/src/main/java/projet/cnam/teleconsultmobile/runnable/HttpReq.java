package projet.cnam.teleconsultmobile.runnable;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import projet.cnam.teleconsultmobile.appPreference;
import projet.cnam.teleconsultmobile.services.ServiceEnvoiFichiers;

/**
 * Created by nikolai on 23/07/16.
 */
public class HttpReq implements Runnable {
    private static final String TAG = HttpReq.class.getSimpleName();
    private static final String HTTP_UPLOAD_IMAGE = "uploadImage";

    /**
     * Handler pour communiquer avec le serviceEnvoiFichier
     */
    private Handler handler;
    private InputStream is;

    public HttpReq(Handler handler, InputStream is) {
        this.handler = handler;
        this.is = is;
    }

    @Override
    public void run() {
        //Récupération de l'image
        //File image = new File(pathToFile);

        HttpClient client = new DefaultHttpClient();
        final HttpParams httpParams = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 3000);

        HttpPost req = new HttpPost();
        String url = "http://" + appPreference.SERVER_ADDR + ":" + appPreference.SERVER_PORT
                + "/" + HTTP_UPLOAD_IMAGE;
        //Liste de paramètres pour la requête
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("paramInt", "3"));
        if (!url.endsWith("?")) {
            url += "?";
        }
        Log.e(TAG, "URL = " + url);
        //Ajout des paramètres à la requête
        url += URLEncodedUtils.format(params, "UTF-8");
        req.setURI(URI.create(url));
        try {
            //ajouterFichierRequetePost(req, image);
            ajouterFichierRequetePost(req, null);
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'ajout du fichier à la requête : ", e);
            e.printStackTrace();
        }

        try {
            HttpResponse response = client.execute(req);
            //Traitement de la réponse
            if (response != null && (response.getStatusLine().getStatusCode() == 200)) {
                String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.e(TAG, "Réponse = " + responseStr);
                /*Gson gson = new GsonBuilder().create();
                MessageServeurTIPE reponseServeur = gson.fromJson(responseStr, MessageServeurTIPE.class);
                if(estRequetePost){ //on retourne le statut
                    handler.obtainMessage(ServiceComm.POST_DONNEES, -1, -1, reponseServeur).sendToTarget();
                } else if (reponseServeur != null) {
                    handler.obtainMessage(type, -1, -1, reponseServeur).sendToTarget();
                }*/

            } else if (response != null) {
                handler.obtainMessage(ServiceEnvoiFichiers.ERROR, -1, -1, response).sendToTarget();
            }
        } catch (IOException e) {
            Log.e(TAG, "impossible de créer le fichier", e);
        } finally {
            client.getConnectionManager().shutdown();
        }

        handler.obtainMessage(ServiceEnvoiFichiers.IMAGE_ENVOYE, -1, -1, 0).sendToTarget();
    }

    /**
     * Ajout le fichier du jour à la requête post
     * @param req   Requête Post
     * @param fichierJour ficher du jour
     * @throws IOException
     */
    public void ajouterFichierRequetePost(HttpPost req, File fichierJour) throws IOException {
        //Ajout du fichier journalier à la requête
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //FileInputStream fis = new FileInputStream(fichierJour);
        ByteArrayBody body = new ByteArrayBody(IOUtils.toByteArray(is),
                "monImage.png");
        builder.addPart("fichierTest", body);
        HttpEntity entity = builder.build();
        req.setEntity(entity);
        req.getAllHeaders();
    }
}
