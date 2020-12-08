package dao;

import models.Department;
import models.DepartmentNews;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oDepartmentNewsDaoTest {
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
    public void addingReviewSetsId() throws Exception {
        Department testDepartment = setupDepartment();
        departmentDao.add(testDepartment);
       DepartmentNews testDepartmentNews = new DepartmentNews("Greetings", "Odile", "Hello", 1,1);
        int originalDepartmentNewsId = testDepartmentNews.getId();
        departmentNewsDao.addDepartmentNews(testDepartmentNews);
        assertEquals(originalDepartmentNewsId,testDepartmentNews.getId());
    }

    @Test
    public void getAll() throws Exception {
        DepartmentNews DepartmentNews1 =setupDepartmentNews();
        DepartmentNews DepartmentNews2 =setupDepartmentNews();
        assertNotEquals(2, departmentNewsDao.getAll().size());
    }
    @Test
    public void deleteById() throws Exception {
        DepartmentNews testDepartmentNews =setupDepartmentNews();
        DepartmentNews otherDepartmentNews =setupDepartmentNews();
        assertEquals(2, departmentNewsDao.getAll().size());
        departmentDao.deleteById(testDepartmentNews.getId());
        assertEquals(1, departmentDao.getAll().size());
    }

    public DepartmentNews setupDepartmentNews() {
        DepartmentNews DepartmentNews = new DepartmentNews("Greetings", "Odile", "Hello", 1,1);
        departmentNewsDao.addDepartmentNews(DepartmentNews);
        return DepartmentNews;
    }


    public Department setupDepartment (){
        Department department = new Department("IT", "TECHNOLOGY TASKS OF THE BUSINESS");
        departmentDao.add(department);
        return department;

    }


}