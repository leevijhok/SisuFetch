package fi.tuni.prog3.sisu;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainViewController {

    public Tab tab_student;
    public Tab tab_plan;
    public TabPane tabPane;
    public GridPane tabStudentView;
    public VBox tabPlanView;
    public PlanViewController tabPlanViewController; // Get a reference to PlanViewController

    public void initialize() {
    }
}