package projet.cnam.teleconsultmobile.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import projet.cnam.teleconsultmobile.Folder;
import projet.cnam.teleconsultmobile.R;

/**
 * This is the ListAdaptor to display all information from folders
 * @author Thibaud Pellissier
 */
public class FoldersAdaptor extends ArrayAdapter<Folder> {

    public FoldersAdaptor(Context context, int resource, List<Folder> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Folder folder = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_folders,parent, false);
        }
        TextView patientName = (TextView) convertView.findViewById(R.id.patient_nameID);
        TextView patientSex = (TextView) convertView.findViewById(R.id.patient_sexeID);
        TextView patientAge = (TextView) convertView.findViewById(R.id.patient_ageID);
        TextView patientPato = (TextView) convertView.findViewById(R.id.patient_patoID);
        TextView patientStatus = (TextView) convertView.findViewById(R.id.patient_statusID);

        patientName.setText(folder.getPatientName());
        patientSex.setText(folder.getPatientSexe());
        patientAge.setText(folder.getPatientAge());
        patientPato.setText(folder.getPatientPato());
        patientStatus.setText(folder.getPatientFolderState());

        return convertView;
    }
}
