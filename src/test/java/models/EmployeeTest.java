package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        Employee testEmployee = setupEmployee();
        assertEquals("Annie", testEmployee.getName());
    }

    @Test
    public void setName() {
        Employee testEmployee = setupEmployee();
        testEmployee.setName("Annie");
        assertNotEquals("Ange", testEmployee.getName());
    }

    @Test
    public void testgetPosition() {
        Employee testEmployee = setupEmployee();
        assertEquals("Secretary", testEmployee.getPosition());
    }

    @Test
    public void testsetPosition() {
        Employee testEmployee = setupEmployee();
        testEmployee.setPosition("Secretary");
        assertNotEquals("Ange", testEmployee.getPosition());
    }
    @Test
    public void testgetRole() {
        Employee testEmployee = setupEmployee();
        assertEquals("Director's assitant", testEmployee.getRole());
    }

    @Test
    public void testsetRole() {
        Employee testEmployee = setupEmployee();
        testEmployee.setRole("Secretary");
        assertNotEquals("HR", testEmployee.getRole());
    }

    @Test
    public void setId() {
        Employee testEmployee = setupEmployee();
        testEmployee.setId(5);
        assertEquals(5, testEmployee.getId());
    }


    @Test
    public void testgetDepartment_idAndsetDepartment_Id() {
        Employee testEmployee = setupEmployee();
        testEmployee.setDepartment_id(5);
        assertEquals(5, testEmployee.getDepartment_id());
    }

    // helper
    public Employee setupEmployee (){
        return new Employee("Annie", "Secretary","Director's assitant");
    }


}