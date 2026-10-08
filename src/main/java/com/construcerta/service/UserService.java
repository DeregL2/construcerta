package com.construcerta.service;

import com.construcerta.dto.AdminUserUpdateForm;
import com.construcerta.dto.RegisterForm;
import com.construcerta.model.Role;
import com.construcerta.model.User;
import com.construcerta.repository.UserRepository;
import com.construcerta.service.exception.UsuarioJaExisteException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrarNovoCliente(RegisterForm form) {
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("As senhas informadas nao coincidem.");
        }
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new UsuarioJaExisteException("Este nome de usuario ja esta em uso.");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new UsuarioJaExisteException("Este e-mail ja esta cadastrado.");
        }

        User user = new User();
        user.setUsername(form.getUsername().trim());
        user.setEmail(form.getEmail().trim().toLowerCase());
        user.setFullName(form.getFullName().trim());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRoles(Set.of(Role.CLIENTE));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public User criarAdminInicial(String username, String email, String rawPassword) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email.toLowerCase());
            admin.setFullName("Administrador ConstruCerta");
            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setRoles(Set.of(Role.ADMIN));
            admin.setEnabled(true);
            return userRepository.save(admin);
        });
    }

    public long contarAdmins() {
        return userRepository.countByRolesContaining(Role.ADMIN);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listarTodos() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User atualizarPerfilDeAcesso(AdminUserUpdateForm form) {
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado."));

        if (form.getRoles() == null || form.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um perfil para o usuario.");
        }

        boolean eraAdmin = user.hasRole(Role.ADMIN);
        boolean continuaAdmin = form.getRoles().contains(Role.ADMIN);
        if (eraAdmin && !continuaAdmin && contarAdmins() <= 1) {
            throw new IllegalStateException("Nao e possivel remover o unico administrador do sistema.");
        }

        user.setRoles(form.getRoles());
        user.setEnabled(form.isEnabled());
        return userRepository.save(user);
    }
}
