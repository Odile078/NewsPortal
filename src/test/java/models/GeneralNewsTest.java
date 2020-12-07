package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeneralNewsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testgetTitle() {
        GeneralNews testGeneralNews = setupGeneralNews();
        assertEquals("Greetings", testGeneralNews.getTitle());
    }

    @Test
    public void testsetTitle() {
        GeneralNews testGeneralNews = setupGeneralNews();
        testGeneralNews.setTitle("Greetings");
        assertNotEquals("Hello", testGeneralNews.getTitle());
    }

    @Test
    public void testgetwrittenby() {
        GeneralNews testGeneralNews = setupGeneralNews();
        assertEquals("Ange", testGeneralNews.getWrittenBy());
    }

    @Test
    public void testsetwrittenby() {
        GeneralNews testGeneralNews = setupGeneralNews();
        testGeneralNews.setWrittenBy("Ange");
        assertNotEquals("Anita", testGeneralNews.getWrittenBy());
    }
    @Test
    public void testgetcontent() {
        GeneralNews testGeneralNews = setupGeneralNews();
        assertEquals("Have a good day Everyone", testGeneralNews.getContent());
    }

    @Test
    public void testsetcontent() {
        GeneralNews testGeneralNews = setupGeneralNews();
        testGeneralNews.setContent("Have a good morning Everyone");
        assertNotEquals("Goodbye", testGeneralNews.getContent());
    }
    @Test
    public void testgetemployee_id() {
        GeneralNews testGeneralNews = setupGeneralNews();
        assertEquals(1, testGeneralNews.getEmployee_id());
    }

    @Test
    public void setDepartment_id() {
        GeneralNews testGeneralNews = setupGeneralNews();
        testGeneralNews.setEmployee_id(10);
        assertNotEquals(1, testGeneralNews.getEmployee_id());
    }

    @Test
    public void testgetIdAndsetId() {
        GeneralNews testGeneralNews = setupGeneralNews();
        testGeneralNews.setId(5);
        assertEquals(5, testGeneralNews.getId());
    }

    // helper
    public GeneralNews setupGeneralNews (){
        return new GeneralNews("Greetings", "Ange","Have a good day Everyone",1);
    }


}