package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class News {
    public int id;
    public String title;
    public String writtenBy;
    public String content;
    public String type;
    public int employee_id;
    public int department_id;
    private long createdat;
    private String formattedCreatedAt;

    private final String TYPE_OF_NEWS="general";

    public News(String title,String writtenBy, String content,int employee_id) {
        this.writtenBy = writtenBy;
        this.title = title;
        this.content = content;
        this.type = TYPE_OF_NEWS;
        this.employee_id = employee_id;
        this.department_id=0;
        this.createdat = System.currentTimeMillis();
        setFormattedCreatedAt();

    }
    public News(String title,String writtenBy, String content,int employee_id,int department_id) {
        this.writtenBy = writtenBy;
        this.title = title;
        this.content = content;
        this.type = "department";
        this.employee_id = employee_id;
        this.department_id=department_id;
        this.createdat = System.currentTimeMillis();
        setFormattedCreatedAt();

    }
    public String getTitle() {
        return title;
    }
    public String getWrittenBy() {
        return writtenBy;
    }
    public String getContent() {
        return content;
    }


    public int getId() {
        return id;
    }
    public int getEmployee_id() {
        return employee_id;
    }
    public int getDepartment_id() {
        return department_id;
    }

    public String getType() {
        return type;
    }

    public long getCreatedat() {
        return createdat;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedat(long createdat) {
        this.createdat = createdat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }


    public String getFormattedCreatedAt(){
        Date date = new Date(createdat);
        String datePatternToUse = "MM/dd/yyyy @ K:mm a"; //see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat(datePatternToUse);
        return sdf.format(date);
    }

    public void setFormattedCreatedAt(){
        Date date = new Date(this.createdat);
        String datePatternToUse = "MM/dd/yyyy @ K:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(datePatternToUse);
        this.formattedCreatedAt = sdf.format(date);
    }

}
