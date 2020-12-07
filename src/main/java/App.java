
import com.google.gson.Gson;
import dao.Sql2oDepartmentNewsDao;
import dao.Sql2oEmployeeDao;
import dao.Sql2oDepartmentDao;
import dao.Sql2oGeneralNewsDao;
import exceptions.ApiException;
import models.Employee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import static spark.Spark.*;

import models.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {
        Sql2oDepartmentDao sql2oDepartmentDao;
        Sql2oEmployeeDao sql2oEmployeeDao;
        Sql2oGeneralNewsDao sql2oGeneralNewsDao;
        Sql2oDepartmentNewsDao sql2oDepartmentNewsDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:postgresql://localhost:5432/companynews";
        Sql2o sql2o = new Sql2o(connectionString, "odile", "123");

        sql2oDepartmentDao = new Sql2oDepartmentDao(sql2o);
        sql2oEmployeeDao = new Sql2oEmployeeDao(sql2o);
        sql2oGeneralNewsDao = new  Sql2oGeneralNewsDao(sql2o);
        sql2oDepartmentNewsDao = new Sql2oDepartmentNewsDao(sql2o);
        conn = sql2o.open();

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        get("/",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"index.hbs");
        },new HandlebarsTemplateEngine());
        //ranger
//department
        //interface
        get("/create/department",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"departmentform.hbs");
        },new HandlebarsTemplateEngine());

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

        //retrieving the department
        get("/view/departments",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("departments",sql2oDepartmentDao.getAll());
            return new ModelAndView(model,"departmentview.hbs");
        },new HandlebarsTemplateEngine());

        //retrive department news
       /*get("/view/location/sightings/:id",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            int idOfLocation= Integer.parseInt(request.params(":id"));
            Department foundLocation= Department.sql2oDepartmentDao.findById(idOfLocation);
            List<DepartmentNews> news=foundLocation.getAll();
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


        //Api

        //
        post("/departments/new", "application/json", (req, res) -> {
            Department department = gson.fromJson(req.body(), Department.class);
            sql2oDepartmentDao.add(department);
            res.status(201);
            res.type("application/json");
            return gson.toJson(department);
        });

        get("/departments","application/json",(request, response) -> {
            if(sql2oDepartmentDao.getAll().size()>0){
                return gson.toJson(sql2oDepartmentDao.getAll());
            }
            else {
                return "{\"message\":\"Sorry, there are no registered departments .\"}";
            }
        });

        get("/department/:id/employees","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentDao.getAllEmployeeForADepartment(id).size()>0){
                return gson.toJson(sql2oDepartmentDao.getAllEmployeeForADepartment(id));
            }
            else {
                return "{\"message\":\"This department has no employees.\"}";
            }
        });

        get("/department/:id","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentDao.findById(id)==null){
                throw new ApiException(404, String.format("No department with id: \"%s\" exists",
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
                throw new ApiException(404, String.format("No department with id: \"%s\" exists",
                        request.params("id")));
            }
            if(sql2oDepartmentDao.getDepartmentNews(id).size()>0){
                return gson.toJson(sql2oDepartmentDao.getDepartmentNews(id));
            }
            else {
                return "{\"message\":\"no news in this department.\"}";
            }
        });

// Employee
        // interface
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
        //Api


        post("/employees/new","application/json",(request, response) -> {
            Employee employee=gson.fromJson(request.body(),Employee.class);
            sql2oEmployeeDao.add(employee);
            response.status(201);
            return gson.toJson(employee);
        });

        post("/add/employee/:employee_id/department/:department_id","application/json",(request, response) -> {

            int employee_id=Integer.parseInt(request.params("employee_id"));
            int department_id=Integer.parseInt(request.params("department_id"));
            Department departments=sql2oDepartmentDao.findById(department_id);
            Employee employees=sql2oEmployeeDao.findById(employee_id);
            if(departments==null){
                throw new ApiException(404, String.format("No department with id: \"%s\" exists",
                        request.params("department_id")));
            }
            if(employees==null){
                throw new ApiException(404, String.format("No employee with id: \"%s\" exists",
                        request.params("employee_id")));
            }
            sql2oDepartmentDao.addEmployeeToDepartment(employees,departments);

            List<Employee> departmentEmmployees=sql2oDepartmentDao.getAllEmployeeForADepartment(departments.getId());

            response.status(201);
            return gson.toJson(departmentEmmployees);
        });



        get("/employees", "application/json", (request, response) -> {

            if(sql2oDepartmentDao.getAll().size() > 0){
                return gson.toJson(sql2oEmployeeDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no employees are currently listed in the database.\"}";
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


//General News
        //Interface
        // creating news interface
        get("/create/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"generalnewsform.hbs");
        },new HandlebarsTemplateEngine());

        //news retrieval
        post("/create/news/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String title=request.queryParams("title");
            String writtenBy=request.queryParams("writtenBy");
            String content=request.queryParams("content");
            int employee_id=Integer.parseInt(request.params("id"));
            int department_id=Integer.parseInt(request.params("id"));
            GeneralNews generalnews=new GeneralNews(title,writtenBy, content,employee_id);
            sql2oGeneralNewsDao.addGeneralNews(generalnews);
            request.session().attribute("item", title);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"generalnewsform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());

        //retrieving the department
        get("/view/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("news",sql2oGeneralNewsDao.getAll());
            return new ModelAndView(model,"generalnewsview.hbs");
        },new HandlebarsTemplateEngine());

        //Api


        post("/news/new/general","application/json",(request, response) -> {

            GeneralNews generalnews =gson.fromJson(request.body(),GeneralNews.class);
            sql2oGeneralNewsDao.addGeneralNews(generalnews);
            response.status(201);
            return gson.toJson(generalnews);
        });

        post("/news/new/general","application/json",(request, response) -> {

            GeneralNews generalnews =gson.fromJson(request.body(),GeneralNews.class);
            sql2oGeneralNewsDao.addGeneralNews(generalnews);
            response.status(201);
            return gson.toJson(generalnews);
        });


        get("/news/general","application/json",(request, response) -> {
            if(sql2oGeneralNewsDao.getAll().size()>0){
                return gson.toJson(sql2oGeneralNewsDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no news are currently listed in the database.\"}";
            }
        });

//Department News
        //Interface
        // creating news interface
        get("/create/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            return new ModelAndView(model,"departmentnewsform.hbs");
        },new HandlebarsTemplateEngine());

        //news retrieval
        post("/create/news/new",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            String title=request.queryParams("title");
            String writtenBy=request.queryParams("writtenBy");
            String content=request.queryParams("content");
            int employee_id=Integer.parseInt(request.params("id"));
            int department_id=Integer.parseInt(request.params("id"));
            DepartmentNews departmentnews=new DepartmentNews(title,writtenBy, content,employee_id,department_id);
            sql2oDepartmentNewsDao.addDepartmentNews(departmentnews);
            request.session().attribute("item", title);
            model.put("item", request.session().attribute("item"));
            return new ModelAndView(model,"departmentnewsform.hbs");
            //return new ModelAndView(model,"employeesuccess.hbs");
        },new HandlebarsTemplateEngine());

        //retrieving the department
        get("/view/news",(request, response) -> {
            Map<String,Object> model=new HashMap<String, Object>();
            model.put("news",sql2oDepartmentNewsDao.getAll());
            return new ModelAndView(model,"departmentnewsview.hbs");
        },new HandlebarsTemplateEngine());

        //Api

        post("/news/new/department","application/json",(request, response) -> {
            DepartmentNews department_news =gson.fromJson(request.body(),DepartmentNews.class);
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
            sql2oDepartmentNewsDao.addDepartmentNews(department_news);
            response.status(201);
            return gson.toJson(department_news);
        });
//exception

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

/*
        after((request, response) ->{
            response.type("application/json");
        });*/


    }
}