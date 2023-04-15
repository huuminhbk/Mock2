package fpt.code.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fpt.code.entities.ERole;
import fpt.code.entities.Role;
import fpt.code.repository.RoleRepository;
import fpt.code.service.RoleService;

@Component
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository roleRepository;

	@Override
	public Optional<Role> findByName(ERole name) {
		return roleRepository.findByName(name);
	}

}
