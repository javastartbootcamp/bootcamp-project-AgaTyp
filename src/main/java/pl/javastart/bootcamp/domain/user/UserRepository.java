package pl.javastart.bootcamp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.javastart.bootcamp.domain.user.role.Role;
import pl.javastart.bootcamp.domain.user.role.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationCode(String activationCode);

    Optional<User> findByAuthKey(String authKey);

    List<User> findByActivated(boolean isActivated);

    Optional<User> findByPasswordResetKey(String key);

    List<User> findAllByRoles_Role(Role role);

}
