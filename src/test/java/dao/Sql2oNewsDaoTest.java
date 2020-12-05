package dao;

import models.Department;
import models.Employee;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oNewsDaoTest {
    private static Connection conn; //these variables are now static.
    private static Sql2oEmployeeDao EmployeeDao; //these variables are now static.
    private static Sql2oDepartmentDao DepartmentDao; //these variables are now static.
    private static Sql2oNewsDao NewsDao; //these variables are now static.

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/newsportal_test"; //connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "odile", "123"); //changed user and pass to null for mac users...Linux & windows need strings
        EmployeeDao = new Sql2oEmployeeDao(sql2o);
        DepartmentDao = new Sql2oDepartmentDao(sql2o);
        NewsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("clearing database");
        EmployeeDao.clearAll(); //clear all restaurants after every test
        DepartmentDao.clearAll(); //clear all restaurants after every test
        NewsDao.clearAll();
    }

    @AfterClass
    public static void shutDown() throws Exception{ //changed to static
        conn.close();
        System.out.println("connection closed");
    }

}