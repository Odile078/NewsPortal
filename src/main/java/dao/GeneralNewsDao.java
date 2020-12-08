package dao;

import models.DepartmentNews;
import models.Employee;
import models.GeneralNews;

import java.util.List;

public interface GeneralNewsDao {
    //create
    void addGeneralNews(GeneralNews generalnews);
    void addEmployeeToGeneralNews(Employee employee, GeneralNews generalNews);


    //read
    List<GeneralNews> getAll();
    List<GeneralNews> getAllgeneralNewsByEmployee(int employee_id);
    // List<News> getAllReviewsByRestaurant(int restaurantId);
    // List<News> getAllReviewsByRestaurantSortedNewestToOldest(int restaurantId);
    GeneralNews findById(int id);
    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}
