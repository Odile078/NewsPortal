package models;

import java.util.Objects;

public class Department {
    private static int id;
    private String name;
    private String description;
    private int size;



    public Department(String name,String description) {
        this.description = description;
        this.name = name;
        this.size = size;

    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getSize() { return size; }
    public static int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSize(int size) { this.size = size; }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id &&
                size == that.size &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, size);
    }
}
