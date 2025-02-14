package com.WrestlingTech.WrestlingTech.Repos;

import com.WrestlingTech.WrestlingTech.model.Role;
import com.WrestlingTech.WrestlingTech.model.User;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        // Create a new User
        User user = new User( "testUser", "password123", "test@example.com", Role.ATHLETE);
        userRepository.save(user);

        // Find user by username
        User foundUser = userRepository.findByUsername("testUser");

        // Assertions
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testUser");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }
}
