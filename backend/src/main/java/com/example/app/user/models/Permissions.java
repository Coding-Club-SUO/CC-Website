package com.example.app.user.models;

import org.springframework.security.core.GrantedAuthority;

public enum Permissions implements GrantedAuthority {

    // All possible permissions a user can acquire
    CREATE_POSTS, EDIT_POSTS, DELETE_POSTS, CREATE_THREAD,
    DELETE_THREAD, EDIT_THREAD, CREATE_EVENTS, 
    EDIT_EVENTS, DELETE_EVENTS, CREATE_ANNOUNCEMENTS, 
    EDIT_ANNOUNCEMENTS, DELETE_ANNOUNCEMENTS, EDIT_CLUB, 
    EDIT_RESEARCH, ROOT_PRIV, EDIT_ACCOUNT;

    // Returns the name of the name of the permission
    @Override
    public String getAuthority() {
        return name();
    }
    
}
