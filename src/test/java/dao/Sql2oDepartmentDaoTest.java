package dao;

import models.Department;
import models.DepartmentNews;
import models.Employee;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oDepartmentDaoTest {
    private static Connection conn;
    private static Sql2oDepartmentDao departmentDao;
    private static Sql2oEmployeeDao employeeDao;
    private static Sql2oGeneralNewsDao generalNewsDao;
    private static Sql2oDepartmentNewsDao departmentNewsDao;

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/companynews_test";
        Sql2o sql2o = new Sql2o(connectionString, "odile", "123");
        departmentDao = new Sql2oDepartmentDao(sql2o);
        employeeDao = new Sql2oEmployeeDao(sql2o);
        generalNewsDao = new Sql2oGeneralNewsDao(sql2o);
        departmentNewsDao = new Sql2oDepartmentNewsDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("clearing database");
        departmentDao.clearAll();
        employeeDao.clearAll();
        generalNewsDao.clearAll();
        departmentNewsDao.clearAll();
    }

    @AfterClass
    public static void shutDown() throws Exception{
        conn.close();
        System.out.println("connection closed");
    }
    @Test
    public void addingDepartmentSetsId() throws Exception {
        Department testdepartment = setupDepartment();
        int originalDepartmentId = testdepartment.getId();
        departmentDao.add(testdepartment);
        assertNotEquals(originalDepartmentId, testdepartment.getId());
    }
    @Test
    public void addeddepartmentsAreReturnedFromGetAll() throws Exception {
        Department testdepartment = setupDepartment();
        assertEquals(1, departmentDao.getAll().size());
    }
    @Test
    public void nodepartmentsReturnsEmptyList() throws Exception {
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void findByIdReturnsCorrectDepartment() throws Exception {
        Department testdepartment = setupDepartment();
        Department otherdepartment = setupDepartment();
        assertEquals(testdepartment, departmentDao.findById(testdepartment.getId()));
    }

    /*@Test
    public void updateCorrectlyUpdatesAllFields() throws Exception {
        Department testdepartment = setupDepartment();
       departmentDao.update(testdepartment.getId(), "a", "b",1);
        Department foundDepartment =departmentDao.findById(testdepartment.getId());
        assertEquals("a", foundDepartment.getName());
        assertEquals("b", foundDepartment.getDescription());

    }*/
    @Test
    public void deleteByIdDeletesCorrectRestaurant() throws Exception {
        Department testdepartment = setupDepartment();
        departmentDao.deleteById(testdepartment.getId());
        assertEquals(0, departmentDao.getAll().size());
    }
    @Test
    public void clearAll() throws Exception {
        Department testdepartment = setupDepartment();
        Department otherdepartment = setupDepartment();
        departmentDao.clearAll();
        assertEquals(0, departmentDao.getAll().size());
    }
    @Test
    public void getAllFoodtypesForARestaurantReturnsFoodtypesCorrectly() throws Exception {
        Employee testEmployee = new Employee("Ange", "secretary","director's assistent");
        employeeDao.add(testEmployee);

        Employee otherEmployee = new Employee("Anita", "secretary","director's assistent");
        employeeDao.add(otherEmployee);

        Department testdepartment = setupDepartment();
        departmentDao.add(testdepartment);
        departmentDao.addEmployeeToDepartment(testEmployee,testdepartment);
       departmentDao.addEmployeeToDepartment(otherEmployee,testdepartment);

        Employee[] employees = {testEmployee, otherEmployee}; //oh hi what is this?

        assertEquals(Arrays.asList(employees), departmentDao.getAllEmployeeForADepartment(testdepartment.getId()));
    }
    @Test
    public void deleteingRestaurantAlsoUpdatesJoinTable() throws Exception {
        Employee testEmployee = new Employee("Ange", "secretary","director's assistent");
        employeeDao.add(testEmployee);

        Department testdepartment = setupDepartment();
        departmentDao.add(testdepartment);

        //Department altdepartment = setupAltDepartment();
       // restaurantDao.add(altRestaurant);

       departmentDao.addEmployeeToDepartment(testEmployee,testdepartment);
       // restaurantDao.addRestaurantToFoodtype(altRestaurant, testFoodtype);

       departmentDao.deleteById(testdepartment.getId());
        assertNotEquals(0, departmentDao.getAllEmployeeForADepartment(testdepartment.getId()).size());
    }

    //helper

    public Department setupDepartment (){
        Department department = new Department("IT", "TECHNOLOGY TASKS OF THE BUSINESS");
        departmentDao.add(department);
        return department;

    }




}