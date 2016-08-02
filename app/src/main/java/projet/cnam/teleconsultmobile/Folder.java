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
    private String patientLastName;
    private String patientMedic;
    private String patientSexe;
    private String patientAge;
    private String patientExam;
    private String patientAvisMedic;
    private String patientAvisRef;
    private String patientFolderState;

    public Folder(String patientName, String patientMedic, String patientSexe, String patientAge, String patientExam, String patientAvisMedic, String patientAvisRef, String patientFolderState) {
        this.patientName = patientName;
        this.patientMedic = patientMedic;
        this.patientSexe = patientSexe;
        this.patientAge = patientAge;
        this.patientExam = patientExam;
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
        return "Examens : "+patientExam;
    }

    public String getPatientAvisMedic() {
        return patientAvisMedic;
    }

    public String getPatientAvisRef() {
        return patientAvisRef;
    }

    public String getPatientFolderState() {
        return patientFolderState;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "patientName='" + patientName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", patientMedic='" + patientMedic + '\'' +
                ", patientSexe='" + patientSexe + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", patientExam='" + patientExam + '\'' +
                ", patientAvisMedic='" + patientAvisMedic + '\'' +
                ", patientAvisRef='" + patientAvisRef + '\'' +
                ", patientFolderState='" + patientFolderState + '\'' +
                '}';
    }
}
