/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonParser;
import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author leevi
 */
public class StudentInfoJson {
    
    private String jsonFileName = "StudentInfo.json";
    private JsonObject allData;
    private JsonArray studentCourses;
    private String studentId;
    private String studentFirstName = "";
    private String studentLastName = "";
    private String degreeProgramme = "";

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getDegreeProgramme() {
        return degreeProgramme;
    }

    public void setDegreeProgramme(String degreeProgramme) {
        this.degreeProgramme = degreeProgramme;
    }
    
    /**
     * Initializes all the StudentInfoJson for a student.
     * @param studentNumber
     * @throws IOException
     */
    public StudentInfoJson(String studentNumber)
            throws IOException
    {
        this.studentId = studentNumber;
        
        JsonParser parser = new JsonParser();
        
        if(Files.notExists(Paths.get(this.jsonFileName)))
        {   
            this.allData = new JsonObject();
            this.studentCourses = new JsonArray();
        }
        else
        {
            try
            {
                this.allData = (JsonObject) parser.parse(new FileReader(this.jsonFileName));
                
                if(this.allData.has(this.studentId))
                {
                    this.studentCourses = (JsonArray) allData.get(this.studentId).getAsJsonObject().get("courses");
                    this.studentFirstName = this.allData.get(this.studentId).getAsJsonObject().get("firstName").getAsString();
                    this.studentLastName = this.allData.get(this.studentId).getAsJsonObject().get("lastName").getAsString();
                    this.degreeProgramme = this.allData.get(this.studentId).getAsJsonObject().get("programme").getAsString();
                }
                else
                {
                    this.studentCourses = new JsonArray();
                }
            }
        
            // Initializes empty allData and studentInfo as empty if IOException.
            catch(IOException ex)
            {
                this.allData = new JsonObject();
                this.studentCourses = new JsonArray();
            }
        }
    }
    
    /**
     * Add course to json
     * Does nothing is course is already in json.
     * @param courseName String of course name
     */
    public void addCompletedCourse(String courseName)
    {
        JsonElement courseElement = new JsonParser().
                parse(removeIllicitChars(courseName));
        
        if(this.studentCourses.contains(courseElement))
        {
            return;
        }
        
        this.studentCourses.add(courseElement);
    }
    
    /**
     * Removes course from json.
     * Does nothing, if the course is not in json.
     * @param courseName String of course name
     */
    public void removeCompletedCourse(String courseName)
    {
        JsonElement courseElement = new JsonParser().
                parse(removeIllicitChars(courseName));
        
        if(!this.studentCourses.contains(courseElement))
        {
            return;
        }
        this.studentCourses.remove(courseElement);
    }
    
    /**
     * Checks if student has already completed the course. (Checks if json has course.)
     * @param courseName String of course name
     * @return true, if yes. false, if no.
     */
    public Boolean isCourseCompleted(String courseName)
    {
        JsonElement courseElement = new JsonParser().
                parse(removeIllicitChars(courseName));
        return this.studentCourses.contains(courseElement);
    }
    
    /**
     * Saves modified userdata to json file.
     * Needs to be called everytime ALL MODIFICATIONS for StudentInfoJson-object ends.
     * Otherwise none of the json modification will not be saved.
     * DO NOT INITIALIZE NEW STUDENTINFOJSON-OBJECTS BEFORE CALLING THIS FUNCTION!!!!!!
     * @throws IOException 
     */
    public void saveJson()
            throws IOException
    {
        JsonObject newJson = new JsonObject();
        newJson.add("courses", this.studentCourses);
        newJson.add("firstName", new JsonPrimitive(this.studentFirstName));
        newJson.add("lastName", new JsonPrimitive(this.studentLastName));
        newJson.add("programme", new JsonPrimitive(this.degreeProgramme));
        this.allData.add(this.studentId, newJson);

        
        try(FileWriter file = new FileWriter(this.jsonFileName))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this.allData);
            file.write(json);
            file.flush();
        }
        catch(IOException ex)
        {
            throw ex;
        }
    }
    
        /**
     * Removes white spaces and special chracters from string.
     * Done to avoid malformed json.
     * @param str String that needs to be modified.
     * @return Modified string.
     */
    public String removeIllicitChars(String str)
    {
        str = str.replaceAll("\\W+","");
        str = str.replaceAll("\\s+","");
        
        return str;
    }
    
    /**
     * ---     THIS FUNCTION IS USED FOR JUnit TESTING! DO NOT USE!     ---
     * Returns student's all courses.
     * @return JsonArray of courses done by the student.
     */
    public JsonArray testCourseData()
    {
        return this.studentCourses;
    }
    
    /**
     * ---     THIS FUNCTION IS USED FOR JUnit TESTING! DO NOT USE!     ---
     * Returns all student data of every student.
     * @return JsonObject of all student data.
     */
    public JsonObject testAllData()
    {
        return this.allData;
    }
    
    /**
     * ---     THIS FUNCTION IS USED FOR JUnit TESTING! DO NOT USE!     ---
     *  Modified version saveJson. Doesn't create or updata json file.
     */
    public void testSaveJson()
    {
        if(this.allData.has(this.studentId))
        {
            this.allData.remove(this.studentId);
        }
        
        this.allData.add(this.studentId, this.studentCourses);
    }

    public String getStudentId() {
        return this.studentId;
    }
}
