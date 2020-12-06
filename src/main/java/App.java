import com.google.gson.Gson;
import dao.Sql2oDepartmentDao;
import dao.Sql2oNewsDao;
import dao.Sql2oEmployeeDao;
import exceptions.ApiException;

import models.Department;
import models.News;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.staticFileLocation;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        Sql2oNewsDao sql2oNewsDao;
        Sql2oEmployeeDao sql2oEmployeeDao;
        Sql2oDepartmentDao sql2oDepartmentDao;
        Connection conn;
        Gson gson = new Gson();
        staticFileLocation("/public");
        //uncomment the line below to run locally
        String connectionString = "jdbc:postgresql://localhost:5432/newsportal";
        //uncomment the line below to run locally,change the following line of code to your credentials
        Sql2o sql2o = new Sql2o(connectionString, "odile", "123");

        //the two lines below are used when using heroku but if you want to run locally comment them
        //String connectionString = "jdbc:postgresql://ec2-50-17-21-170.compute-1.amazonaws.com:5432/d8b8ehu0safpui"; //!
        //Sql2o sql2o = new Sql2o(connectionString, "mihpivzxyyqmlv", "5b4f9d76874ad368465a325b3993140263c6d254771908c3d283842d54fcad11");

        sql2oDepartmentDao=new Sql2oDepartmentDao(sql2o);
        sql2oNewsDao=new Sql2oNewsDao(sql2o);
        sql2oEmployeeDao=new Sql2oEmployeeDao(sql2o);
        conn=sql2o.open();

        //read employees,news,departments
        get("/employees", "application/json", (request, response) -> {

            if(sql2oDepartmentDao.getAll().size() > 0){
                return gson.toJson(sql2oEmployeeDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no employees are currently listed in the database.\"}";
            }
        });

        get("/departments","application/json",(request, response) -> {
            if(sql2oDepartmentDao.getAll().size()>0){
                return gson.toJson(sql2oDepartmentDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no departments are currently listed in the database.\"}";
            }
        });
        get("/news/general","application/json",(request, response) -> {
            if(sql2oNewsDao.getAll().size()>0){
                return gson.toJson(sql2oNewsDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no news are currently listed in the database.\"}";
            }
        });
        get("/employee/:id/departments","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oEmployeeDao.getAllEmployeeByDepartment(id).size()>0){
                return gson.toJson(sql2oEmployeeDao.getAllEmployeeByDepartment(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but employee is in no department.\"}";
            }
        });
        get("/employee/:id", "application/json", (request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oEmployeeDao.findById(id)==null){
                throw new ApiException(404, String.format("No employee with the id: \"%s\" exists",
                        request.params("id")));
            }
            else {
                return gson.toJson(sql2oEmployeeDao.findById(id));
            }
        });
        get("/department/:id/employees","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentDao.getAllEmployeeForADepartment(id).size()>0){
                return gson.toJson(sql2oDepartmentDao.getAllEmployeeForADepartment(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but department has no employees.\"}";
            }
        });
        get("/department/:id","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentDao.findById(id)==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            else {
                return gson.toJson(sql2oDepartmentDao.findById(id));
            }
        });
        get("/news/department/:id","application/json",(request, response) -> {

            int id=Integer.parseInt(request.params("id"));
            Department departments=sql2oDepartmentDao.findById(id);
            if(departments==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            if(sql2oDepartmentDao.getDepartmentNews(id).size()>0){
                return gson.toJson(sql2oDepartmentDao.getDepartmentNews(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but no news in this department.\"}";
            }
        });



        //create employee,news,department

        post("/employees/new","application/json",(request, response) -> {
            Employee employee=gson.fromJson(request.body(),Employee.class);
            sql2oEmployeeDao.add(employee);
            response.status(201);
            return gson.toJson(employee);
        });
        post("/departments/new","application/json",(request, response) -> {
            Department department =gson.fromJson(request.body(),Department.class);
            sql2oDepartmentDao.add(department);
            response.status(201);
            return gson.toJson(department);
        });

        post("/news/new/general","application/json",(request, response) -> {

            News news =gson.fromJson(request.body(),News.class);
            sql2oNewsDao.addNews(news);
            response.status(201);
            return gson.toJson(news);
        });
        post("/news/new/department","application/json",(request, response) -> {
            News department_news =gson.fromJson(request.body(),News.class);
            Department departments=sql2oDepartmentDao.findById(department_news.getDepartment_id());
            Employee employees=sql2oEmployeeDao.findById(department_news.getEmployee_id());
            if(departments==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            if(employees==null){
                throw new ApiException(404, String.format("No employee with the id: \"%s\" exists",
                        request.params("id")));
            }
            sql2oNewsDao.addNews(department_news);
            response.status(201);
            return gson.toJson(department_news);
        });

        post("/news/new/general","application/json",(request, response) -> {

            News news =gson.fromJson(request.body(),News.class);
            sql2oNewsDao.addNews(news);
            response.status(201);
            return gson.toJson(news);
        });
        post("/add/employee/:employee_id/department/:department_id","application/json",(request, response) -> {

            int employee_id=Integer.parseInt(request.params("employee_id"));
            int department_id=Integer.parseInt(request.params("department_id"));
            Department departments=sql2oDepartmentDao.findById(department_id);
            Employee employees=sql2oEmployeeDao.findById(employee_id);
            if(departments==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("department_id")));
            }
            if(employees==null){
                throw new ApiException(404, String.format("No employee with the id: \"%s\" exists",
                        request.params("employee_id")));
            }
            sql2oDepartmentDao.addEmployeeToDepartment(employees,departments);

            List<Employee> departmentEmmployees=sql2oDepartmentDao.getAllEmployeeForADepartment(departments.getId());

            response.status(201);
            return gson.toJson(departmentEmmployees);
        });
        //FILTERS
        exception(ApiException.class, (exception, request, response) -> {
            ApiException err = exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            response.type("application/json");
            response.status(err.getStatusCode());
            response.body(gson.toJson(jsonMap));
        });


        after((request, response) ->{
            response.type("application/json");
        });


    }
}
