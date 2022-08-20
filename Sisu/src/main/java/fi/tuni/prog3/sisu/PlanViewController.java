package fi.tuni.prog3.sisu;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PlanViewController {
    public VBox courseSelectionList;
    public TreeView<String> courseTree;

    // This is used to separate course names from course credits in the tree view
    // This needs to be a unique string that does not appear in any course name
    private static final String CREDITS_SEPARATOR = " - ";

    public void initialize() throws IOException {
        courseTree.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setText(null);
                    else setText(item);
                }
            };
            cell.setOnMouseClicked(event -> {
                if (! cell.isEmpty()) {
                    TreeItem<String> treeItem = cell.getTreeItem();
                    initCourseSelectionList(treeItem.getValue());
                }
            });
            return cell ;
        });
        if(Sisu.studentInfo.getDegreeProgramme() != null) buildTree(Sisu.studentInfo.getDegreeProgramme());
    }

    /**
     * Updates the course tree with a new degree programme
     * @param degreeProgramme the new degree programme
     */
    public void updateView(String degreeProgramme) {
        courseTree.getRoot().getChildren().clear();
        try {
            buildTree(degreeProgramme);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the course with the given name
     * @param courseName the name of the course to find
     * @return JsonParser.CourseInfo object of the course
     */
    private JsonParser.CourseInfo findCourse(String courseName) {
        ArrayList<JsonParser.CourseInfo> courses = JsonParser.getCourseList();
        for (JsonParser.CourseInfo course : courses)
            if (course.getCourseName().equals(courseName)) return course;
        return null;
    }

    /**
     * Finds child courses for a given parent
     * @param parentCourse the parent course
     * @return ArrayList of child courses
     */
    private ArrayList<JsonParser.CourseInfo> getChildCourses(JsonParser.CourseInfo parentCourse) {
        ArrayList<JsonParser.CourseInfo> courses = JsonParser.getCourseList();
        ArrayList<JsonParser.CourseInfo> children = new ArrayList<>();
        boolean start = false;
        for (JsonParser.CourseInfo course : courses)
            if (Objects.equals(course.getGroupId(), parentCourse.getGroupId())) start = true;
            else if (start) if (Objects.equals(course.getCourseType(), "CourseUnit")) children.add(course);
            else break;
        return children;
    }

    /**
     * Finds child StudyModules for a given GroupingModule
     * @param parentModule the parent course
     * @return ArrayList of child courses
     */
    private ArrayList<JsonParser.CourseInfo> getChildStudyModules(JsonParser.CourseInfo parentModule) {
        ArrayList<JsonParser.CourseInfo> courses = JsonParser.getCourseList();
        ArrayList<JsonParser.CourseInfo> children = new ArrayList<>();
        boolean start = false;
        for (JsonParser.CourseInfo course : courses)
            if (Objects.equals(course.getGroupId(), parentModule.getGroupId())) start = true;
            else if (start) {
                if (Objects.equals(course.getCourseType(), "StudyModule")) children.add(course);
                else if (!course.getCourseType().equals("CourseUnit")) break;
            }
        return children;
    }

    /**
     * Toggles course completion status
     * @param completed the new status of the course
     * @param courseName the name of the course
     */
    private void toggleCourseCompletion(boolean completed, String courseName){
        if(completed) Sisu.studentInfo.addCompletedCourse(courseName);
        else Sisu.studentInfo.removeCompletedCourse(courseName);
    }

    /**
     * Builds the course selection list for a given parent
     * @param parent the parent element other than "CourseUnit"
     */
    private void initCourseSelectionList(String parent) {
        courseSelectionList.getChildren().clear();
        JsonParser.CourseInfo parentCourse = findCourse(stripCredits(parent));
        if(parentCourse == null || Objects.equals(parentCourse.getCourseType(), "CourseUnit")) return;
        ArrayList<JsonParser.CourseInfo> children = getChildCourses(parentCourse);
        for (JsonParser.CourseInfo child : children) {
            CheckBox checkBox = new CheckBox(child.getCourseName());
            checkBox.setSelected(Sisu.studentInfo.isCourseCompleted(child.getCourseName()));
            checkBox.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    toggleCourseCompletion(new_val, child.getCourseName());
                    updateCredits();
                });
            courseSelectionList.getChildren().add(new HBox(checkBox));
        }
    }

    private String stripCredits(String courseName)
    {
        if(!courseName.contains(CREDITS_SEPARATOR)) return courseName;
        return courseName.substring(0, courseName.indexOf(CREDITS_SEPARATOR));
    }

    /**
     * Returns the total amount of completed credits
     * @return int
     */
    private int totalCreditsCompleted() {
        int credits = 0;
        for (JsonParser.CourseInfo course : JsonParser.getCourseList())
            if (Sisu.studentInfo.isCourseCompleted(course.getCourseName())) credits += course.getCourseCredits();
        return credits;
    }

    /**
     * Returns the total amount of credits for the given course or Module
     * @return int
     */
    private int creditsRequired(String courseName) {
        JsonParser.CourseInfo course = findCourse(courseName);
        if(course == null) return 0;
        return course.getCourseCredits();
    }

    /**
     * Returns the total amount of completed credits for the given course or Module
     * @return int
     */
    private int creditsCompleted(String courseName) {
        JsonParser.CourseInfo course = findCourse(courseName);
        if(course == null) return 0;
        switch (course.getCourseType()) {
            case "CourseUnit":
                return Sisu.studentInfo.isCourseCompleted(courseName) ? course.getCourseCredits() : 0;
            case "StudyModule":
                int credits = 0;
                for (JsonParser.CourseInfo child : getChildCourses(course))
                    credits += creditsCompleted(child.getCourseName());
                return credits;
            case "GroupingModule":
                credits = 0;
                for (JsonParser.CourseInfo child : getChildStudyModules(course))
                    credits += creditsCompleted(child.getCourseName());
                return credits;
            default:
                return 0;
        }
    }

    /**
     * Updates the credits in the tree view
     */
    private void updateCredits() {
        ArrayList<JsonParser.CourseInfo> courses = JsonParser.getCourseList();
        courseTree.getRoot().setValue(String.format("%s%s%d/%dop", stripCredits(courseTree.getRoot().getValue()),
                CREDITS_SEPARATOR, totalCreditsCompleted(), creditsRequired(courses.get(0).getCourseName())));
        for (TreeItem<String> item : courseTree.getRoot().getChildren()) recurseUpdateCredits(item);
    }


    /**
     * Helper function for updateCredits() to recursively update the credits in the tree view
     * @param item TreeItem
     */
    private void recurseUpdateCredits(TreeItem<String> item) {
        String courseName = stripCredits(item.getValue());
        item.setValue(String.format("%s%s%d/%dop", courseName, CREDITS_SEPARATOR,
                creditsCompleted(courseName), creditsRequired(courseName)));
        if(item.getChildren().size() == 0) return;
        for (TreeItem<String> child : item.getChildren())
            recurseUpdateCredits(child);
    }

    /**
     * Builds the tree for a given degree programme
     * @param degreeProgramme the degree programme
     * @throws IOException if the degree cannot be found
     */
    private void buildTree(String degreeProgramme) throws IOException {
        if(degreeProgramme == null || degreeProgramme.equals("")) return;
        JsonParser.getDegreeId(degreeProgramme);
        ArrayList<JsonParser.CourseInfo> courses = JsonParser.getCourseList();
        courseTree.getRoot().setValue(String.format("%s%s%d/%dop", degreeProgramme, CREDITS_SEPARATOR,
                totalCreditsCompleted(), creditsRequired(courses.get(0).getCourseName())));
        TreeItem<String> previousGroupingModule = null;
        TreeItem<String> previousStudyModule = null;

        for (JsonParser.CourseInfo course : courses) {
            String textValue = String.format("%s%s%d/%dop", course.getCourseName(), CREDITS_SEPARATOR,
                    creditsCompleted(course.getCourseName()), creditsRequired(course.getCourseName()));
            switch (course.getCourseType()) {
                case "GroupingModule":
                    previousGroupingModule = new TreeItem<>(textValue);
                    previousStudyModule = null;
                    courseTree.getRoot().getChildren().add(previousGroupingModule);
                    break;
                case "StudyModule":
                    previousStudyModule = new TreeItem<>(textValue);
                    if (previousGroupingModule != null) previousGroupingModule.getChildren().add(previousStudyModule);
                    else courseTree.getRoot().getChildren().add(previousStudyModule);
                    break;
                case "CourseUnit":
                    if (previousStudyModule != null)
                        previousStudyModule.getChildren().add(new TreeItem<>(textValue));
                    else if (previousGroupingModule != null)
                        previousGroupingModule.getChildren().add(new TreeItem<>(textValue));
                    else courseTree.getRoot().getChildren().add(new TreeItem<>(textValue));
                    break;
            }
        }
    }
}
