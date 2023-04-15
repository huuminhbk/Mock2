package fpt.code.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fpt.code.entities.ERole;
import fpt.code.entities.Role;

@Service
public interface RoleService {
	public Optional<Role> findByName(ERole name);
}
