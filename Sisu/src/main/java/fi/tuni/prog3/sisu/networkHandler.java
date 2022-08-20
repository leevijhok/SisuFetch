/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;

/**
 *
 * @author leevi
 */
public class networkHandler {
    
    
    
    /**
     * Gets all degrees in JSON String format.
     * @return String of module JSON. Empty String if Exception.
     * @throws MalformedURLException 
     */
    public String getAllDegrees()
            throws MalformedURLException, IOException
    {
        try
        {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/"
                + "module-search?curriculumPeriodId=uta-lvv-2021&universityId="
                + "tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
        URL url = new URL(urlStr);
        String jsonStr = new String(url.openStream().readAllBytes());
        
        return jsonStr;
        }
        
        catch(MalformedURLException ex)
        {
            return "";
        }
        catch(IOException ex)
        {
            return "";
        }
    }
    
    /**
     * Gets the Module JSON with ID in String format.
     * @param id ID of the study module.
     * @return String of module JSON. Empty String if Exception.
     * @throws MalformedURLException 
     */
    public String getModuleById(String id)
            throws MalformedURLException, IOException
    {
        try
        {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/modules/" + id;
        URL url = new URL(urlStr);
        String jsonStr = new String(url.openStream().readAllBytes());
        
        return jsonStr;
        }
        
        catch(MalformedURLException ex)
        {
            return "";
        }
        catch(IOException ex)
        {
            return "";
        }
    }
    
    
    /**
     * Gets the Module JSON with group-ID in String format.
     * @param groupId Group ID of the study module.
     * @return String of module JSON. Empty String if Exception.
     * @throws MalformedURLException 
     */
    public String getModuleByGroupId(String groupId)
            throws MalformedURLException, IOException
    {
        try
        {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/modules/"
                + "by-group-id?groupId=" + groupId + 
                "&universityId=tuni-university-root-id";
        URL url = new URL(urlStr);
        String jsonStr = new String(url.openStream().readAllBytes());
        
        return jsonStr;
        }
        
        catch(MalformedURLException ex)
        {
            return "";
        }
        catch(IOException ex)
        {
            return "";
        }
    }
    
    /**
     * Gets the Course JSON with group-ID in String format.
     * @param groupId Group ID of the course.
     * @return String of course JSON. Empty String if Exception.
     * @throws MalformedURLException 
     */
    public String getCourseByGroupId(String groupId)
            throws MalformedURLException, IOException
    {
        try
        {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/course-units/"
                + "by-group-id?groupId=" + groupId + 
                "&universityId=tuni-university-root-id";
        URL url = new URL(urlStr);
        String jsonStr = new String(url.openStream().readAllBytes());
        
        return jsonStr;
        }
        
        catch(MalformedURLException ex)
        {
            return "";
        }
        catch(IOException ex)
        {
            return "";
        }
    }
}
