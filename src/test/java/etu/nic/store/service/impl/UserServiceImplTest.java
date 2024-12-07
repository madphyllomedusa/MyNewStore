package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.entity.User;
import etu.nic.store.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;


    @ParameterizedTest
    @MethodSource("provideTestDataForExtractUserIdFromContext")
    void testExtractUserIdFromContext(Object principal, Long expectedUserId) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);

        if (principal instanceof String) {
            String email = (String) principal;
            User user = new User();
            user.setId(expectedUserId);
            user.setEmail(email);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        }

        Long userId = userService.extractUserIdFromContext();

        assertEquals(expectedUserId, userId);

        if (principal instanceof String) {
            verify(userRepository, times(1)).findByEmail((String) principal);
        } else {
            verify(userRepository, never()).findByEmail(anyString());
        }
    }


    @ParameterizedTest
    @MethodSource("provideTestDataForFindUserIdByEmail")
    void testFindUserIdByEmail(String email, Long expectedUserId, boolean shouldThrowException) {
        if (shouldThrowException) {
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> userService.findUserIdByEmail(email));
        } else {
            User user = new User();
            user.setId(expectedUserId);
            user.setEmail(email);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            Long userId = userService.findUserIdByEmail(email);
            assertEquals(expectedUserId, userId);
        }
    }

    static Stream<Object[]> provideTestDataForExtractUserIdFromContext() {
        return Stream.of(
                new Object[]{"user1@example.com", 1L},
                new Object[]{null, null},
                new Object[]{12345, null}
        );
    }

    static Stream<Object[]> provideTestDataForFindUserIdByEmail() {
        return Stream.of(
                new Object[]{"user1@example.com", 1L, false},
                new Object[]{"unknown@example.com", null, true}
        );
    }
}
