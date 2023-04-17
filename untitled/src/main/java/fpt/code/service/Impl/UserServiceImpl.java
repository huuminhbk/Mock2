package fpt.code.service.Impl;

import fpt.code.entities.User;
import fpt.code.repository.UserRepository;
import fpt.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public void save(@Valid User user) {
		userRepository.save(user);
	}

	@Override
	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public Page<User> findUsersByRole(Pageable pageable) {
		return userRepository.findUsersByRole(pageable);
	}

}
