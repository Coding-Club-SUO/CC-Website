package com.example.app.user.models;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.app.common.Faculty;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Nullable
    @Column(nullable = true)
    private String photoLink;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Faculty faculty = Faculty.NA;
    @Size(max = 250, message = "Bio cannot exceed 250 characters")
    @Nullable
    @Column(nullable = false)
    private String bio;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date dateJoined;
    private Boolean active = true; //Toggled by admin for baning

    @ElementCollection
    private Set<String> activeIn;

    private Integer comments = 0;
    private Integer posts = 0;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private EnumSet<Permissions> authorities = EnumSet.of(Permissions.CREATE_POSTS, Permissions.EDIT_POSTS, 
        Permissions.DELETE_POSTS, Permissions.CREATE_THREAD,
        Permissions.DELETE_THREAD, Permissions.EDIT_THREAD, 
        Permissions.EDIT_ACCOUNT);

    private Boolean expired = false; //Unused account flag
    private Boolean locked = false; //Block login attempts.

    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getId() { return id; }
    @Override
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhotoLink() { return photoLink; }
    public Faculty getFaculty() { return faculty; }
    public String getBio() { return bio; }
    public Date getDateJoined() { return dateJoined; }
    public Boolean getActive() { return active; }
    public Set<String> getActiveIn() { return activeIn; }
    public Integer getComments() { return comments; }
    public Integer getPosts() { return posts; }
    @Override
    public EnumSet<Permissions> getAuthorities() { return authorities; }
    public Boolean getExpired() { return expired; }
    public Boolean getLocked() { return locked; }

    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setPhotoLink(String photoLink) { this.photoLink = photoLink; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public void setBio(String bio) { this.bio = bio; }
    public void setDateJoined(Date dateJoined) { this.dateJoined = dateJoined; }
    public void setActive(Boolean active) { this.active = active; }
    public void setActiveIn(Set<String> activeIn) { this.activeIn = activeIn; }
    public void setComments(Integer comments) { this.comments = comments; }
    public void setPosts(Integer posts) { this.posts = posts; }
    public void setAuthorities(EnumSet<Permissions> authorities) { this.authorities = authorities; }
    public void setExpired(Boolean expired) { this.expired = expired; }
    public void setLocked(Boolean locked) { this.locked = locked; }
}