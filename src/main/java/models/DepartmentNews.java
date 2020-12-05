package models;

public class DepartmentNews extends News {

    private String news_type;
    private int department_id;
    private final String TYPE_OF_NEWS="department";
    public DepartmentNews(String name, String tittle, String writtenBy, String content, int employee_id, int department_id) {
        super(name, tittle, writtenBy, content, employee_id);
        this.department_id = department_id;
        this.type = TYPE_OF_NEWS;
    }
    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getDepartment_id() {
        return department_id;
    }




}
