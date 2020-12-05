package dao;

import models.DepartmentNews;
import models.News;

import java.util.List;

public interface NewsDao {
    //create
    void add(News news);
    void addDepartmentNews(DepartmentNews departmentNews);

    //read
    List<News> getAll();
   // List<News> getAllReviewsByRestaurant(int restaurantId);
    // List<News> getAllReviewsByRestaurantSortedNewestToOldest(int restaurantId);
    News findById(int id);
    //update
    //omit for now

    //delete
    //void deleteById(int id);
    void clearAll();
}
