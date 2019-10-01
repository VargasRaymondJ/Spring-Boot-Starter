package com.bah.comet.cometapi.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomToken extends UsernamePasswordAuthenticationToken {

    private int userID;

    public CustomToken(String username, String credentials,
                       Collection<? extends GrantedAuthority> authorities, int userID) {
        super(username, credentials, authorities);
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
