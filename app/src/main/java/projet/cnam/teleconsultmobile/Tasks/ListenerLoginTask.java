package projet.cnam.teleconsultmobile.Tasks;

/**
 * Created by thibaud on 02/07/16.
 */
public interface ListenerLoginTask {
    void onLoginTaskTrue(String auth);
    void onLoginTaskFalse();
}
