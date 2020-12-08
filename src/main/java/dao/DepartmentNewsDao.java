package dao;

import models.Department;
import models.DepartmentNews;
import models.Employee;

import java.util.List;

public interface DepartmentNewsDao {
    //create

    void addDepartmentNews(DepartmentNews departmentNews);
    void addEmployeeToDepartmentNews(Employee employee, DepartmentNews departmentNews);
    void addDepartmentToDepartmentNews(Department department, DepartmentNews departmentNews);

    //read
    List<DepartmentNews> getAll();
    List<DepartmentNews> getAllDepartmentNewsByDepartmet(int department_id);
    List<DepartmentNews> getAllDepartmentNewsByEmployee(int employee_id);
    // List<News> getAllReviewsByRestaurant(int restaurantId);
    // List<News> getAllReviewsByRestaurantSortedNewestToOldest(int restaurantId);
    DepartmentNews findById(int id);
    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}
