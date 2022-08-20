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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author leevi
 */

// Some tests haven't been finished yet.
// I'll finish them, when I'll find time to do so.

public class networkHandlerTest {
    
    private String allDegreesTestSource = "allDegreesTest.txt";
    
    public networkHandlerTest() {
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
     * Test of getModuleById method, of class networkHandler.
     */
    @Test
    public void testGetModuleById() throws Exception {
        System.out.println("getModuleById");
        
        try
        {
            String id = "otm-d2728f44-3e53-4bad-84c4-dbd257ac0f34";
            networkHandler instance = new networkHandler();
            Boolean expResult = true;
            
            Boolean result = false;
            
            // Test will be tried for 3 times,
            // because api doesn't alway return the same outcome.
            for(int i=0; i<3; i++)
            {
                String text = instance.getModuleById(id);
                
                if(text.contains("Arkkitehtuurin kandidaattiohjelma"))
                {
                    result = true;
                    break;
                }
            }

            assertEquals(expResult, result);
        }
        
        catch(Exception ex)
        {
            assertEquals(true,false);
        }
    }

    /**
     * Test of getModuleByGroupId method, of class networkHandler.
     */
    @Test
    public void testGetModuleByGroupId() throws Exception {
        System.out.println("getModuleByGroupId");
        
        try
        {
            String id = "tut-dp-g-1100";
            networkHandler instance = new networkHandler();
            Boolean expResult = true;
            
            Boolean result = false;
            
            // Test will be tried for 3 times,
            // because api doesn't alway return the same outcome.
            for(int i=0; i<3; i++)
            {
                String text = instance.getModuleByGroupId(id);
                if(text.contains("Arkkitehtuurin kandidaattiohjelma"))
                {
                    result = true;
                    break;
                }
            }

            assertEquals(expResult, result);
        }
        
        catch(Exception ex)
        {
            assertEquals(true,false);
        }
    }

    /**
     * Test of getCourseByGroupId method, of class networkHandler.
     */
    @Test
    public void testGetCourseByGroupId() throws Exception {
        System.out.println("getCourseByGroupId");
        
        try
        {
            String groupId = "tut-cu-g-48874";
            networkHandler instance = new networkHandler();
            Boolean expResult = true;
            
            Boolean result = false;
            
            // Test will be tried for 3 times,
            // because api doesn't alway return the same outcome.
            for(int i=0; i<3; i++)
            {
                String text = instance.getCourseByGroupId(groupId);
                if(text.contains("Urban Planning and Design"))
                {
                    result = true;
                    break;
                }
            }
            
            assertEquals(expResult, result);
        }
        catch(Exception ex)
        {
            assertEquals(true,false);
        }
        
    }
    
}
