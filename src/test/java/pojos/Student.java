package pojos;

import lombok.Data;

import java.util.List;

@Data
public class Student extends Person{
    private int year;
    private String department;
    private List<Course> courses;
}
