/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;

/**
 *
 * @author leevi
 */
public class StudentInfoJsonTest {
    
    public StudentInfoJsonTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addCompletedCourse method, of class StudentInfoJson.
     */
    @Test
    public void testAddCompletedCourse1() throws IOException, MalformedJsonException{
        System.out.println("addCompletedCourse");
        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "!@[]{}|¤¤´´+äöäöäöäö";
        
        StudentInfoJson instance = new StudentInfoJson("ID123456789");
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
       
        JsonArray testArray = instance.testCourseData();
        
        Boolean result = testArray.contains(new JsonParser().
                parse(instance.removeIllicitChars(courseName1)));
        Boolean expResult = true;
        assertEquals(expResult,result);
    }

    /**
     * Test of addCompletedCourse method, of class StudentInfoJson.
     */
    @Test
    public void testAddCompletedCourse2() throws IOException, MalformedJsonException{
        System.out.println("addCompletedCourse");
        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "!@[]{}|¤¤´´+äöäöäöäö";
        
        StudentInfoJson instance = new StudentInfoJson("ID123456789");
        instance.addCompletedCourse(courseName1);
       
        JsonArray testArray = instance.testCourseData();
        
        Boolean result = testArray.contains(new JsonParser().
                parse(instance.removeIllicitChars(courseName2)));
        Boolean expResult = false;
        assertEquals(expResult,result);
    }
    
    /**
     * Test of removeCompletedCourse method, of class StudentInfoJson.
     */
    @Test
    public void testRemoveCompletedCourse1() throws IOException{
        System.out.println("removeCompletedCourse");
        
        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "Huuhaa humanismin perusteet 1";
        
        StudentInfoJson instance = new StudentInfoJson("ID123456789");
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
        
        instance.removeCompletedCourse(courseName1);
        JsonArray testArray = instance.testCourseData();
        
        Boolean result = testArray.contains(new JsonParser().parse((instance.removeIllicitChars(courseName1))));
        Boolean expResult = false;
        assertEquals(expResult,result);
    }

    /**
     * Test of removeCompletedCourse method, of class StudentInfoJson.
     */
    @Test
    public void testRemoveCompletedCourse2() throws IOException{
        System.out.println("removeCompletedCourse");
        
        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "Huuhaa humanismin perusteet 1";
        
        StudentInfoJson instance = new StudentInfoJson("ID123456789");
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
        
        instance.removeCompletedCourse(courseName2);
        JsonArray testArray = instance.testCourseData();
        
        Boolean result = testArray.contains(new JsonParser().parse((instance.removeIllicitChars(courseName1))));
        Boolean expResult = true;
        assertEquals(expResult,result);
    }
    
    /**
     * Test of isCourseCompleted method, of class StudentInfoJson.
     */
    @Test
    public void testIsCourseCompleted1() throws IOException{
        System.out.println("isCourseCompleted");

        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "Huuhaa humanismin perusteet 1";
        
        StudentInfoJson instance = new StudentInfoJson("ID123456789");
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
        
        Boolean result = instance.isCourseCompleted(courseName1);
        Boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of isCourseCompleted method, of class StudentInfoJson.
     */
    @Test
    public void testIsCourseCompleted2() throws IOException{
        System.out.println("isCourseCompleted");

        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "Huuhaa humanismin perusteet 1";
        
        StudentInfoJson instance = new StudentInfoJson("ID123456789");
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
        
        Boolean result = instance.isCourseCompleted("this course is not done");
        Boolean expResult = false;
        assertEquals(expResult, result);
    }
    
    /**
     * Test of saveJson method, of class StudentInfoJson.
     */
    @Test
    public void testSaveJson1() throws IOException {
        System.out.println("saveJson");
        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "Elamamkoulun perusteet 1";
        String id = "ID123456789";
        
        StudentInfoJson instance = new StudentInfoJson(id);
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
        
        // We are using a slightly modified function of saveJson.
        // Doesn't modify json file.
        instance.testSaveJson();
        JsonObject allStudentData = instance.testAllData();
        Boolean result = allStudentData.has(id);
        Boolean expResult = true;
        
        assertEquals(expResult,result);
        
    }
    
    /**
     * Test of saveJson method, of class StudentInfoJson.
     */
    @Test
    public void testSaveJson2() throws IOException {
        System.out.println("saveJson");
        String courseName1 = "!@#$%^&*()-';,./?><+abdd";
        String courseName2 = "Elamamkoulun perusteet 1";
        String id = "ID123456789";
        
        StudentInfoJson instance = new StudentInfoJson(id);
        instance.addCompletedCourse(courseName1);
        instance.addCompletedCourse(courseName2);
        
        // We are using a slightly modified function of saveJson.
        // Doesn't modify json file.
        instance.testSaveJson();
        JsonObject allStudentData = instance.testAllData();
        Boolean result = allStudentData.has("42069_does not exist");
        Boolean expResult = false;
        
        assertEquals(expResult,result);
        
    }
}
