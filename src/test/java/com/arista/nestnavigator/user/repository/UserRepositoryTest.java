package com.arista.nestnavigator.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.arista.nestnavigator.user.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    User user1;
    @BeforeEach
    void setup(){
        user1 = new User("John", "Doe", "john.doe@example.com", "1234567890", "john_doe", "password123");
        userRepository.save(user1);
    }
    @AfterEach
    void tearDown(){
        user1 =null;
        userRepository.deleteAll();
    }
    @Test
    void getUserbyEmailTest() throws Exception{
        User user = userRepository.getUserByEmail(user1.getEmail());
        assertThat(user.getUsername()).isEqualTo(user1.getUsername());
    }

    @Test
    void getUserbyUsernameTest() throws Exception{
        User user = userRepository.getUserByUsername(user1.getUsername());
       assertThat(user.getId()).isEqualTo(user1.getId());
    }

    @Test
    void getUserbyPhoneTest() throws Exception{
        User user = userRepository.getUserByPhone(user1.getPhone());
        assertThat(user.getId()).isEqualTo(user1.getId());
    }
}
