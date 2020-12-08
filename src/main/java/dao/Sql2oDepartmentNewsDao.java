package dao;
import models.Employee;
import models.Department;
import models.DepartmentNews;
import models.GeneralNews;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oDepartmentNewsDao implements DepartmentNewsDao{
    private final Sql2o sql2o;
    public Sql2oDepartmentNewsDao(Sql2o sql2o) { this.sql2o = sql2o; }


    @Override
    public void addDepartmentNews(DepartmentNews departmentNews) {
        try(Connection con=sql2o.open()) {
            String sql="INSERT INTO departmentnews  (title,writtenby, content,createdat, employee_id,department_id) VALUES (:tittle,:writtenBy,:content,:createdat, :employee_id,:department_id)";
            //String joinEmployee="INSERT INTO employees_departmentnews (employee_id,departmentnews_id) VALUES (:employee_id,:departmentnews_id_id)";
            //String joinDepartment="INSERT INTO departments_departmentnews (department_id,departmentnews_id) VALUES (:department_id,:departmentnews_id_id)";
            int id= (int) con.createQuery(sql,true)
                    .bind(departmentNews)
                    .executeUpdate()
                    .getKey();
            departmentNews.setId(id);



        }catch (Sql2oException e){
            System.out.println(e);
        }
    }

    @Override
    public void addEmployeeToDepartmentNews(Employee employee, DepartmentNews departmentNews) {
        String sql="INSERT INTO employees_departmentnews (employee_id,departmentnews_id) VALUES (:employee_id,:departmentnews_id_id)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("employee_id", Employee.getId())
                    .addParameter("departmentnews_id", DepartmentNews.getId())
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public void addDepartmentToDepartmentNews(Department department, DepartmentNews departmentNews) {
        String sql="INSERT INTO departments_departmentnews (department_id,departmentnews_id) VALUES (:department_id,:departmentnews_id_id)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("department_id", Department.getId())
                    .addParameter("departmentnews_id", DepartmentNews.getId())
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public List<DepartmentNews> getAll() {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM departmentnews";
            return con.createQuery(sql,true)
                    .executeAndFetch(DepartmentNews.class);

        }
    }

    @Override
    public DepartmentNews findById(int id) {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM departmentnews WHERE id=:id";
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(DepartmentNews.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from departmentnews WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        try (Connection con=sql2o.open()){
            String sql="DELETE FROM departments";
            String sqlNews="DELETE FROM departmentnews";
            String sqlEmployeesDepartments="DELETE FROM employees_departments";
            con.createQuery(sql).executeUpdate();
            con.createQuery(sqlEmployeesDepartments).executeUpdate();
            con.createQuery(sqlNews).executeUpdate();

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
}
