package com.projects.blog.services;

import com.projects.blog.exceptionHandlers.exception.resource.RoleNotFound;
import com.projects.blog.models.Role;
import com.projects.blog.repositories.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findOrCreateRole(String name) {
        Role role;

        try {
            role = roleRepository.findByName(name).orElseThrow(RoleNotFound::new);
        } catch (RoleNotFound roleNotFound) {
            role = Role.builder()
                       .name(name).build();
            roleRepository.save(role);
        }

        return role;
    }
}