package com.construcerta.dto;

import com.construcerta.model.Role;

import java.util.Set;

public class AdminUserUpdateForm {
    private String userId;
    private Set<Role> roles;
    private boolean enabled;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
