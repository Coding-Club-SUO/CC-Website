package com.example.app.user;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.example.app.common.Faculty;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Faculty faculty;
    private String bio;
    private Date dateJoined;
    private Boolean active;
    private Boolean online;

    @ElementCollection
    private List<String> activeIn;

    private Integer comments;
    private Integer posts;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Collection<Permissions> authorities;

    private Boolean expired;
    private Boolean locked;

    public User() {}

    public User(String username, String password, String email, Faculty faculty, String bio,
                Date dateJoined, Boolean active, Boolean online, List<String> activeIn,
                Integer comments, Integer posts, Collection<Permissions> authorities,
                Boolean expired, Boolean locked) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.faculty = faculty;
        this.bio = bio;
        this.dateJoined = dateJoined;
        this.active = active;
        this.online = online;
        this.activeIn = activeIn;
        this.comments = comments;
        this.posts = posts;
        this.authorities = authorities;
        this.expired = expired;
        this.locked = locked;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Faculty getFaculty() { return faculty; }
    public String getBio() { return bio; }
    public Date getDateJoined() { return dateJoined; }
    public Boolean getActive() { return active; }
    public Boolean getOnline() { return online; }
    public List<String> getActiveIn() { return activeIn; }
    public Integer getComments() { return comments; }
    public Integer getPosts() { return posts; }
    public Collection<Permissions> getAuthorities() { return authorities; }
    public Boolean getExpired() { return expired; }
    public Boolean getLocked() { return locked; }

    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public void setBio(String bio) { this.bio = bio; }
    public void setDateJoined(Date dateJoined) { this.dateJoined = dateJoined; }
    public void setActive(Boolean active) { this.active = active; }
    public void setOnline(Boolean online) { this.online = online; }
    public void setActiveIn(List<String> activeIn) { this.activeIn = activeIn; }
    public void setComments(Integer comments) { this.comments = comments; }
    public void setPosts(Integer posts) { this.posts = posts; }
    public void setAuthorities(Collection<Permissions> authorities) { this.authorities = authorities; }
    public void setExpired(Boolean expired) { this.expired = expired; }
    public void setLocked(Boolean locked) { this.locked = locked; }
}