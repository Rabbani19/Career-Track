package com.careertrack.service;

import com.careertrack.dto.UserDTO;
import com.careertrack.enums.Role;
import com.careertrack.model.User;
import com.careertrack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserDTO userDTO;

    @Test
    void registerUser_shouldHashPasswordAndSetDefaults() {

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("hashedPassword");

        service.registerUser(
                "Yasir",
                "Rabbani",
                "yasir",
                "yasir@example.com",
                "plainPassword"
        );

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Yasir", savedUser.getFirstName());
        assertEquals("Rabbani", savedUser.getLastName());
        assertEquals("Yasir Rabbani", savedUser.getFullName());
        assertEquals("yasir", savedUser.getUsername());
        assertEquals("yasir@example.com", savedUser.getEmail());

        assertEquals("hashedPassword", savedUser.getPassword());

        assertNotEquals(
                "plainPassword",
                savedUser.getPassword()
        );

        assertEquals(Role.USER, savedUser.getRole());
        assertTrue(savedUser.getActive());

        verify(passwordEncoder).encode("plainPassword");
    }

    @Test
    void existsByUsername_shouldDelegateToRepository() {

        when(userRepository.existsByUsername("yasir"))
                .thenReturn(true);

        boolean result =
                service.existsByUsername("yasir");

        assertTrue(result);

        verify(userRepository)
                .existsByUsername("yasir");
    }

    @Test
    void existsByEmail_shouldDelegateToRepository() {

        when(userRepository.existsByEmail("yasir@example.com"))
                .thenReturn(true);

        boolean result =
                service.existsByEmail("yasir@example.com");

        assertTrue(result);

        verify(userRepository)
                .existsByEmail("yasir@example.com");
    }

    @Test
    void getCurrentUser_shouldReturnUserWhenFound() {

        User user = new User();
        user.setUsername("yasir");

        when(userRepository.findByUsername("yasir"))
                .thenReturn(Optional.of(user));

        User result =
                service.getCurrentUser("yasir");

        assertSame(user, result);
    }

    @Test
    void getCurrentUser_shouldThrowExceptionWhenNotFound() {

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.getCurrentUser("unknown")
                );

        assertTrue(
                exception.getMessage()
                        .contains("User not found")
        );
    }

    @Test
    void updateProfile_shouldUpdateOnlyProvidedFields() {

        User user = new User();

        user.setFullName("Old Name");
        user.setPhone("1111111111");
        user.setLocation("Bangalore");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userDTO.getFullName())
                .thenReturn("New Name");

        when(userDTO.getPhone())
                .thenReturn(null);

        when(userDTO.getLocation())
                .thenReturn("Mysore");

        when(userDTO.getCurrentCompany())
                .thenReturn(null);

        when(userDTO.getCurrentRole())
                .thenReturn(null);

        when(userDTO.getExperienceYears())
                .thenReturn(null);

        when(userDTO.getSkills())
                .thenReturn(null);

        when(userDTO.getLinkedinUrl())
                .thenReturn(null);

        when(userDTO.getGithubUrl())
                .thenReturn(null);

        when(userDTO.getPortfolioUrl())
                .thenReturn(null);

        service.updateProfile(1L, userDTO);

        assertEquals("New Name", user.getFullName());

        // Existing value must remain unchanged
        assertEquals("1111111111", user.getPhone());

        assertEquals("Mysore", user.getLocation());

        verify(userRepository).save(user);
    }

    @Test
    void updateProfile_shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.updateProfile(999L, userDTO)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }
}