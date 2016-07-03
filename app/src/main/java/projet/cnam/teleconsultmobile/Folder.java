package projet.cnam.teleconsultmobile;

/**
 * Folder :
 * ========
 * This class represent the folder of patient
 * Is using for the arrayadptor
 * @author Thibaud Pellissier
 */
public class Folder {
    private String patientName;
    private String patientMedic;
    private String patientSexe;
    private String patientAge;
    private String patientPato;
    private String patientAvisMedic;
    private String patientAvisRef;
    private int patientFolderState;

    public Folder(String patientName, String patientMedic, String patientSexe, String patientAge, String patientPato, String patientAvisMedic, String patientAvisRef, int patientFolderState) {
        this.patientName = patientName;
        this.patientMedic = patientMedic;
        this.patientSexe = patientSexe;
        this.patientAge = patientAge;
        this.patientPato = patientPato;
        this.patientAvisMedic = patientAvisMedic;
        this.patientAvisRef = patientAvisRef;
        this.patientFolderState = patientFolderState;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientMedic() {
        return patientMedic;
    }

    public String getPatientSexe() {
        return "Sexe : "+patientSexe;
    }

    public String getPatientAge() {
        return "Age : "+patientAge;
    }

    public String getPatientPato() {
        return "Pathologie : "+patientPato;
    }

    public String getPatientAvisMedic() {
        return patientAvisMedic;
    }

    public String getPatientAvisRef() {
        return patientAvisRef;
    }

    public int getPatientFolderState() {
        return patientFolderState;
    }
}
