package com.bah.comet.cometapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Won't return null fields to the UI
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(schema = "comet", name = "app_user")
@ApiModel(value = "User", description = "Represents a user of the application")
public class User extends ResourceSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")

    private Integer userId;

    // Variable name matches column name, so no need for annotation
    private String email;

    // Variable name matches column name, so no need for annotation
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_on", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "last_login")
    private Date lastLogin;

    private String roles;

    @JsonIgnore
    private Date deleted;

    // We want this as a field, but not mapped to any DB column, so we mark it as Transient
    @Transient
    private String newPassword;

    public User(Integer userId, String email, String password, String firstName, String lastName, Date lastLogin, String roles, Date deleted, String newPassword) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastLogin = lastLogin;
        this.roles = roles;
        this.deleted = deleted;
        this.newPassword = newPassword;
    }

    public User() {
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}

