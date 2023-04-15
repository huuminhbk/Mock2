package fpt.code.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fpt.code.entities.User;

@Service
public interface UserService {
	public Page<User> findUsersByRole(Pageable pageable);

	public void save(@Valid User user);

	public Optional<User> findById(Integer id);

	public void deleteById(Integer id);

	public Optional<User> findByUsername(String username);

	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);

}
