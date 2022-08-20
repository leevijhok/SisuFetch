package fi.tuni.prog3.sisu;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StudentViewController {

    public TextField firstName;
    public TextField lastName;
    public TextField studentNumber;
    public ComboBox<String> studyProgramme;

    public void initialize() {
        firstName.setText(Sisu.studentInfo.getStudentFirstName());
        lastName.setText(Sisu.studentInfo.getStudentLastName());
        studentNumber.setText(Sisu.studentInfo.getStudentId());
        initProgramList();
        initListeners();
    }

    private void initProgramList(){
        try {
            JsonParser.getAllDegreeNames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(String program : JsonParser.getProgramNameList()) {
            studyProgramme.getItems().add(program);
        }
        if(Sisu.studentInfo.getDegreeProgramme() != null) studyProgramme.setValue(Sisu.studentInfo.getDegreeProgramme());
    }

    private void initListeners(){
        studyProgramme.setOnHidden(event -> {
            Sisu.studentInfo.setDegreeProgramme(studyProgramme.getValue());
            Sisu.planViewController.updateView(studyProgramme.getValue());
        });
        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            Sisu.studentInfo.setStudentFirstName(newValue);
        });
        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            Sisu.studentInfo.setStudentLastName(newValue);
        });
    }

}
