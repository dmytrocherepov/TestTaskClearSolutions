package org.example.testtaskclearsolutions.repository;

import org.example.testtaskclearsolutions.model.Role;
import org.example.testtaskclearsolutions.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
