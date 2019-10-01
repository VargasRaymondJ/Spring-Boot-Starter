package com.bah.comet.cometapi.model;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;

@Entity
@Table(schema = "comet", name = "course_catalog")
public class Course extends ResourceSupport {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer courseId;
    @Column(name = "class_title")
    private String courseTitle;
    @Column(name = "class_description")
    private String classDescription;
    @Column(name = "instructor_first_name")
    private String instructorFirstName;
    @Column(name = "instructor_last_name")
    private String instructorLastName;
    private String subject;
    private int credits;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public String getInstructorFirstName() {
        return instructorFirstName;
    }

    public void setInstructorFirstName(String instructorFirstName) {
        this.instructorFirstName = instructorFirstName;
    }

    public String getInstructorLastName() {
        return instructorLastName;
    }

    public void setInstructorLastName(String instructorLastName) {
        this.instructorLastName = instructorLastName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
