package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepartmentTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        Department testDepartment = setupDepartment();
        assertEquals("dessert", testDepartment.getName());
    }

    @Test
    public void setName() {
        Department testDepartment = setupDepartment();
        testDepartment.setName("IT");
        assertNotEquals("IT", testDepartment.getName());
    }

    @Test
    public void getDescription() {
        Department testDepartment = setupDepartment();
        assertEquals("TECHNOLOGY TASKS OF THE BUSINESS", testDepartment.getDescription());
    }

    @Test
    public void setDescription() {
        Department testDepartment = setupDepartment();
        testDepartment.setName("TECHNOLOGY TASKS OF THE BUSINESS");
        assertNotEquals("TECHNOLOGY ", testDepartment.getDescription());
    }

    @Test
    public void setId() {
        Department testDepartment = setupDepartment();
        testDepartment.setId(5);
        assertEquals(5, testDepartment.getId());
    }

    // helper
    public Department setupDepartment (){
        return new Department("IT", "TECHNOLOGY TASKS OF THE BUSINESS");
    }


}