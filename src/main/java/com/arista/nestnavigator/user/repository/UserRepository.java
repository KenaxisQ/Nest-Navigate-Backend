package com.arista.nestnavigator.user.repository;

import com.arista.nestnavigator.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User getUserByEmail(String email);
    User getUserByPhone(String phone);
    User getUserByUsername(String username);
    User findByUsername(String username);
}
