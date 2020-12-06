import com.google.gson.Gson;
import dao.DepartmentDao;
import dao.Sql2oDepartmentDao;
import dao.Sql2oNewsDao;
import dao.Sql2oEmployeeDao;
import exceptions.ApiException;

import models.Department;
import models.News;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;


import java.util.ArrayList;
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


        sql2oDepartmentDao=new Sql2oDepartmentDao(sql2o);
        sql2oNewsDao=new Sql2oNewsDao(sql2o);
        sql2oEmployeeDao=new Sql2oEmployeeDao(sql2o);
        conn=sql2o.open();

        //Home page
        get("/",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"index.hbs");
        },new HandlebarsTemplateEngine());
 //employee
        //creating employee interface
        get("/create/employee",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"employeeform.hbs");
        },new HandlebarsTemplateEngine());
          //employee retrieval
        post("/create/employee/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String name=request.queryParams("name");
            String position=request.queryParams("position");
            String role=request.queryParams("role");
            Employee employee=new Employee(name, position, role);
            sql2oEmployeeDao.add( employee);
            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"employeeform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());
           //retrieving the employee
        get("/view/employees",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("employees",sql2oEmployeeDao.getAll());
            return new ModelAndView(model,"employeeview.hbs");
        },new HandlebarsTemplateEngine());

            //retrieve employee news



//department
        //department

        //creating department interface
        get("/create/department",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"departmentform.hbs");
        },new HandlebarsTemplateEngine());
            //department saving

        post("/create/department/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String name=request.queryParams("name");
            String description=request.queryParams("description");
            String size=request.queryParams("size");
            Department department=new Department(name,description);
            sql2oDepartmentDao.add( department);
            request.session().attribute("item", name);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"departmentform.hbs");
            //return new ModelAndView(model,"departmentsuccess.hbs");
        },new HandlebarsTemplateEngine());
             //department retrieval

            //retrieving the department
        get("/view/departments",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("departments",sql2oDepartmentDao.getAll());
            return new ModelAndView(model,"departmentview.hbs");
        },new HandlebarsTemplateEngine());

            //retrive department news
     /*   get("/view/location/sightings/:id",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            int idOfLocation= Integer.parseInt(request.params(":id"));
            Department foundLocation= Department.sql2oDepartmentDao.findById(idOfLocation);
            List<News> news=foundLocation.getAll();
            ArrayList<String> animals=new ArrayList<String>();
            ArrayList<String> types=new ArrayList<String>();
            for (RegSighting sighting : news){
                String animal_name=RegAnimal.find(sighting.getRegAnimal_id()).getName();
                String animal_type=RegAnimal.find(sighting.getRegAnimal_id()).getType();
                animals.add(animal_name);
                types.add(animal_type);
            }
            model.put("sightings",news);
            model.put("animals",animals);
            model.put("types",types);
            model.put("locations",RegLocation.all());
            return new ModelAndView(model,"locationview.hbs");
        },new HandlebarsTemplateEngine());*/

//news
        // creating news interface
        get("/create/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"newsform.hbs");
        },new HandlebarsTemplateEngine());

        //news retrieval
        post("/create/news/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String title=request.queryParams("title");
            String writtenBy=request.queryParams("writtenBy");
            String content=request.queryParams("content");
            int employee_id=Integer.parseInt(request.params("id"));
            int department_id=Integer.parseInt(request.params("id"));
            News news=new News(title,writtenBy, content,employee_id,department_id);
            sql2oNewsDao.addNews(news);
            request.session().attribute("item", title);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"employeeform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());

        //retrieving the department
        get("/view/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("news",sql2oNewsDao.getAll());
            return new ModelAndView(model,"newsview.hbs");
        },new HandlebarsTemplateEngine());


//API
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
