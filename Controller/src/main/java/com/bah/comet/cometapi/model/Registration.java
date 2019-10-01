package com.bah.comet.cometapi.model;

import javax.persistence.*;

@Entity
@Table(schema = "comet", name = "course_registration")
public class Registration {

   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   @Column(name = "registration_id", columnDefinition = "serial")
   private Integer registrationId;

    @Column(name = "user_id")
   private Integer userId;

    @Column(name = "course_id")
   private Integer courseId;

    public Registration() {
    }

    public Registration(Integer userId, Integer courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseID) {
        this.courseId = courseID;
    }
}
