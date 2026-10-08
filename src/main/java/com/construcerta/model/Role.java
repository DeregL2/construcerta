package com.construcerta.model;

public enum Role {
    CLIENTE("Cliente"),
    VENDEDOR("Vendedor"),
    ADMIN("Administrador");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
