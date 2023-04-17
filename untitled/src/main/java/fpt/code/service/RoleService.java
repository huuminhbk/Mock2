package fpt.code.service;

import fpt.code.entities.ERole;
import fpt.code.entities.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleService {
	public Optional<Role> findByName(ERole name);
}
