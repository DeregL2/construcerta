package com.construcerta.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {
    @Test
    void authorityTemPrefixoRole() {
        assertEquals("ROLE_CLIENTE", Role.CLIENTE.getAuthority());
        assertEquals("ROLE_VENDEDOR", Role.VENDEDOR.getAuthority());
        assertEquals("ROLE_ADMIN", Role.ADMIN.getAuthority());
    }

    @Test
    void existemNoMinimoTresPerfis() {
        assertEquals(3, Role.values().length, "A atividade exige no minimo 3 perfis de usuario.");
    }
}
