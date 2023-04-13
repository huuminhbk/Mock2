package fpt.code.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fpt.code.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  
  @Query(value="SELECT * FROM users u  JOIN  user_roles r ON u.user_id = r.user_id  JOIN roles rs ON r.role_id = rs.role_id WHERE rs.name ='ROLE_USER'",nativeQuery = true)
  Page<User> findUsersByRole(Pageable pageable) ;
  
  @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
  public User findByVerificationCode(String code);

}