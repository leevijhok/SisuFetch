package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


/**
 * A class for parsing a string of JSON-format.
 * @author Miika Valli
 */
public class JsonParser
{
    /**
     * ArrayList to store all the courses' infos.
     */
    private static ArrayList<CourseInfo> courseList;
    
    /**
     * ArrayList to store all the program names.
     */
    private static ArrayList<String> programNameList;
    
    /**
     * A nested class that saves the needed info of a module or a course.
     */
    public static class CourseInfo
    {
        private final String courseName;
        private final String groupId;
        private final String courseType;
        private final int courseCredits;
        
        /**
         * Constructs a CourseInfo object for storing info of the class.
         * @param courseName The name of the course.
         * @param groupId The group id of the course.
         * @param courseType The type of the course.
         * @param courseCredits the credits the student receives from the course.
         */
        public CourseInfo(String courseName, String groupId, String courseType, 
                int courseCredits)
        {
            this.courseName = courseName;
            this.groupId = groupId;
            this.courseType = courseType;
            this.courseCredits = courseCredits;
        }
        
        /**
         * Returns the name of the course.
         * @return The name of the course.
         */
        public String getCourseName()
        {
            return courseName;
        }

        /**
         * Returns the group id of the course.
         * @return The group id of the course.
         */
        public String getGroupId()
        {
            return groupId;
        }
        
        /**
         * Returns the type of the course.
         * @return The type of the course.
         */
        public String getCourseType()
        {
            return courseType;
        }

        /**
         * Returns the credits of the course.
         * @return The credits of the course.
         */
        public int getCourseCredits()
        {
            return courseCredits;
        }
    } 
    
    /**
     * An empty constructor.
     */
    public JsonParser()
    {
    }
    
    /**
     * Returns the ArrayList of courses.
     * @return The ArrayList of courses.
     */
    public static ArrayList<CourseInfo> getCourseList()
    {
        return courseList;
    }
    
    /**
     * Returns the ArrayList of program names.
     * @return The ArrayList of program names.
     */
    public static ArrayList<String> getProgramNameList()
    {
        return programNameList;
    }
    
    /**
     * Initializes courseList and programNameList. Purely for unit tests.
     */
    public static void initLists()
    {
        courseList = new ArrayList<>();
        programNameList = new ArrayList<>();
    }
    
    /**
     * Adds a course to courseList. Purely for unit tests.
     * @param info Info of one course.
     */
    public static void setCourseList(CourseInfo info)
    {
        initLists();
        courseList.add(info);
    }
    
    /**
     * Adds a program's name to programNameList. Purely for unit tests.
     * @param program Name of one program.
     */
    public static void setProgramNameList(String program)
    {
        initLists();
        programNameList.add(program);
    }
    
    /**
     * Parses a string of JSON-format of courseUnit-type.
     * @param groupId The group id of a course.
     * @throws MalformedURLException
     * @throws IOException
     */   
    public static void parseCourseUnitJson(String groupId)
            throws MalformedURLException, IOException
    {
        String jsonStrToParse = new networkHandler()
                                .getCourseByGroupId(groupId);
        
        JsonObject root = new Gson().fromJson(jsonStrToParse, JsonArray.class)
                                    .get(0).getAsJsonObject();
        
        String courseName = "";
        
        if (root.get("name").getAsJsonObject().has("en"))
        {
            courseName = root.get("name").getAsJsonObject().get("en")
                                                           .getAsString();
        }
        
        else
        {
            courseName = root.get("name").getAsJsonObject().get("fi")
                                                           .getAsString();
        }
        
        int courseCredits = root.get("credits").getAsJsonObject().get("min")
                                                                 .getAsInt();
        String courseType = "CourseUnit";
        
        CourseInfo info = new CourseInfo(courseName, groupId, courseType,
                                                              courseCredits);
        courseList.add(info);
    }
    
    /**
     * Adds modules to a temporary list if they meet certain conditions.
     * @param ruleObj Module as JsonObject.
     * @param tempList The temporary list containing all the modules.
     * @return Returns the temporary list containing JsonObjects.
     */
    public static ArrayList<JsonObject> addToTempList(JsonObject ruleObj, 
                                      ArrayList<JsonObject> tempList)
    {
        if (ruleObj.has("rules"))
        {
            JsonArray ruleArr = ruleObj.get("rules").getAsJsonArray();

            for (int i = 0; i < ruleArr.size(); ++i)
            {
                ruleObj = ruleArr.get(i).getAsJsonObject();
                
                if (ruleObj.get("type").getAsString().equals("ModuleRule") ||
                    ruleObj.get("type").getAsString().equals("CourseUnitRule"))
                {
                    tempList.add(ruleObj);
                }
                
                addToTempList(ruleObj, tempList);
            }
        }
        
        else if (ruleObj.has("rule"))
        {
            ruleObj = ruleObj.get("rule").getAsJsonObject();
            addToTempList(ruleObj, tempList);
        }
        
        return tempList;
    }
    
    /**
     * Parses module as string of JSON-format.
     * @param groupId The group id of a module.
     * @throws MalformedURLException
     * @throws IOException
     */  
    public static void parseModuleJsonByGroupId(String groupId)
            throws MalformedURLException, IOException
    {
        String jsonStrToParse = new networkHandler()
                                .getModuleByGroupId(groupId);
        JsonObject root = new Gson().fromJson(jsonStrToParse, JsonArray.class)
                                    .get(0).getAsJsonObject();
        
        String courseType = root.get("type").getAsString();
        String courseName = "";
        
        if (root.get("name").getAsJsonObject().has("en"))
        {
            courseName = root.get("name").getAsJsonObject().get("en")
                                                .getAsString();
        }
        
        else
        {
            courseName = root.get("name").getAsJsonObject().get("fi")
                                                .getAsString();
        }
        
        int courseCredits = 0;
        
        if (!("GroupingModule".equals(courseType)))
        {
            courseCredits = root.get("targetCredits").getAsJsonObject()
                                                     .get("min").getAsInt();
        }
        
        CourseInfo info = new CourseInfo(courseName, groupId, courseType,
                                                              courseCredits);
        courseList.add(info);
        
        JsonObject ruleObj = root.get("rule").getAsJsonObject();
        
        ArrayList<JsonObject> tempList = new ArrayList<>();
        
        tempList = addToTempList(ruleObj, tempList);
        
        for (int i = 0; i < tempList.size(); ++i)
        {
            ruleObj = tempList.get(i).getAsJsonObject();
        
            if (ruleObj.get("type").getAsString().equals("ModuleRule"))
            {
                parseModuleJsonByGroupId(ruleObj.get("moduleGroupId")
                                                .getAsString());
            }

            else if (ruleObj.get("type").getAsString().equals("CourseUnitRule"))
            {
                parseCourseUnitJson(ruleObj.get("courseUnitGroupId")
                                           .getAsString());
            }
        }
    }
    
    /**
     * Parses a string of JSON-format to find the group id of a study program.
     * @param studyProgram The name of the study program.
     * @throws MalformedURLException
     * @throws IOException
     */ 
    public static void getDegreeId(String studyProgram)
            throws MalformedURLException, IOException
    {
        courseList = new ArrayList<>();
        String groupId = "";
        
        String jsonStrToParse = new networkHandler().getAllDegrees();
        JsonObject root = new Gson().fromJson(jsonStrToParse, JsonObject.class);
        JsonArray programArr = root.get("searchResults").getAsJsonArray();
        
        for (int i = 0; i < programArr.size(); i++)
        {
            JsonObject programObj = programArr.get(i).getAsJsonObject();
            
            if (programObj.get("name").getAsString().equals(studyProgram))
            {
                groupId = programObj.get("groupId").getAsString();
            }
        }
        
        if (!(groupId.equals("")))
        {
            parseModuleJsonByGroupId(groupId);
        }
    }
    
    /**
     * Parses a string of JSON-format to get names of all the study programs.
     * @throws MalformedURLException
     * @throws IOException
     */ 
    public static void getAllDegreeNames()
            throws MalformedURLException, IOException
    {
        programNameList = new ArrayList<>();
        String programName = "";
        
        String jsonStrToParse = new networkHandler().getAllDegrees();
        JsonObject root = new Gson().fromJson(jsonStrToParse, JsonObject.class);
        JsonArray programArr = root.get("searchResults").getAsJsonArray();
        
        for (int i = 0; i < programArr.size(); i++)
        {
            JsonObject programObj = programArr.get(i).getAsJsonObject();
            programName = programObj.get("name").getAsString();
            programNameList.add(programName);
        }
    }
}
