package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class JsonParser. Not all unit tests are implemented because
 * the scope of the tests would have been huge, for example
 * the test for getAllDegreeNames. Also the exceptions MalformedURLException
 * and IOException are not tested because the program should be designed in such
 * way, that those exceptions aren't possible. For example, the URL's are always
 * the same, except for the groupId's which are also found from the URL's, so
 * they should be correct also.
 * @author Miika Valli
 */
public class JsonParserTest
{
    
    public JsonParserTest()
    {
    }

    /**
     * Test of getCourseList method, of class JsonParser.
     */
    @Test
    public void testGetCourseList()
    {
        System.out.println("getCourseList");
        JsonParser.CourseInfo info = new JsonParser.CourseInfo("Mathematic 1", 
                                                        "1234","CourseUnit", 5);
        ArrayList<JsonParser.CourseInfo> expResult = new ArrayList<>();
        expResult.add(info);
        
        JsonParser.setCourseList(info);
        ArrayList<JsonParser.CourseInfo> result = JsonParser.getCourseList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProgramNameList method, of class JsonParser.
     */
    @Test
    public void testGetProgramNameList()
    {
        System.out.println("getProgramNameList");
        String name = "Master's Programme in Information Technology";
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add(name);
        
        JsonParser.setProgramNameList(name);
        ArrayList<String> result = JsonParser.getProgramNameList();
        assertEquals(expResult, result);
    }

    /**
     * Test of parseCourseUnitJson method, of class JsonParser.
     * @throws MalformedURLException URL isn't correct one.
     * @throws IOException Something in input/output has gone wrong
     */
    @Test
    public void testParseCourseUnitJson()
            throws MalformedURLException, IOException
    {
        System.out.println("parseCourseUnitJson");
        String groupId = "tut-cu-g-35899";
        JsonParser.CourseInfo info = new JsonParser.CourseInfo("Differential "
                + "and Integral Calculus", "tut-cu-g-35899","CourseUnit", 5);
        ArrayList<JsonParser.CourseInfo> expResult = new ArrayList<>();
        expResult.add(info);
        
        JsonParser.initLists();
        JsonParser.parseCourseUnitJson(groupId);
        ArrayList<JsonParser.CourseInfo> result = JsonParser.getCourseList();
        
        assertEquals(expResult.get(0).getCourseName() + 
                expResult.get(0).getGroupId() + expResult.get(0).getCourseType()
                + expResult.get(0).getCourseCredits(),
                result.get(0).getCourseName() + result.get(0).getGroupId() +
                result.get(0).getCourseType() +
                result.get(0).getCourseCredits());
    }
    
    /**
     * Test of addToTempList method, of class JsonParser.
     */
    @Test
    public void testAddToTempList()
    {
        System.out.println("addToTempList");
        JsonObject ruleObj = new JsonObject();
        ruleObj.addProperty("courseUnitGroupId", "tut-cu-g-48229");
        ruleObj.addProperty("type", "CourseUnitRule");
        ruleObj.addProperty("localId",
                            "otm-4c38eec2-97d1-404a-9176-32774cd2aeb7");
        
        ArrayList<JsonObject> expResult = new ArrayList<>();
        ArrayList<JsonObject> result = JsonParser.addToTempList(ruleObj,
                                                                expResult);
        expResult.add(ruleObj);
        
        assertEquals(expResult.get(0).get("courseUnitGroupId").getAsString() + 
                expResult.get(0).get("type").getAsString() + 
                expResult.get(0).get("localId").getAsString(),
                result.get(0).get("courseUnitGroupId").getAsString() + 
                result.get(0).get("type").getAsString() +
                result.get(0).get("localId").getAsString());
    }
}
