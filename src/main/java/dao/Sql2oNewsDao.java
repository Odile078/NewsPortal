package dao;

import models.DepartmentNews;
import models.News;
import org.sql2o.Sql2o;

import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

public class Sql2oNewsDao implements NewsDao{
    private final Sql2o sql2o;
    public Sql2oNewsDao(Sql2o sql2o) { this.sql2o = sql2o; }
    @Override
    public void add(News news) {
        String sql = "INSERT INTO news (title,writtenby, content,type, employee_id,department_id, createdat) VALUES (:tittle,:writtenBy,:content,:type, :employee_id,:department_id, :createdat)"; //if you change your model, be sure to update here as well!
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(news)
                    .executeUpdate()
                    .getKey();
            news.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void addDepartmentNews(DepartmentNews departmentNews) {
        try(Connection con=sql2o.open()) {
            String sql="INSERT INTO news  (title,writtenby, content,type, employee_id,department_id, createdat) VALUES (:tittle,:writtenBy,:content,:type, :employee_id,:department_id, :createdat)";
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
    public List<News> getAll() {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM news";
            return con.createQuery(sql,true)
                    .executeAndFetch(News.class);

        }
    }

    @Override
    public News findById(int id) {
        try(Connection con=sql2o.open()) {
            String sql="SELECT * FROM news WHERE id=:id";
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(News.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from news WHERE id=:id";
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
            String sqlNews="DELETE FROM news";
            String sqlEmployeesDepartments="DELETE FROM employees_departments";
            con.createQuery(sql).executeUpdate();
            con.createQuery(sqlEmployeesDepartments).executeUpdate();
            con.createQuery(sqlNews).executeUpdate();

        }catch (Sql2oException e){
            System.out.println(e);
        }

    }
}
