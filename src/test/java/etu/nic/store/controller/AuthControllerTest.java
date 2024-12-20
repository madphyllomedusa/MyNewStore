package etu.nic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import etu.nic.store.config.JwtService;
import etu.nic.store.config.SecurityConfig;
import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.model.dto.JwtAuthenticationResponse;
import etu.nic.store.model.dto.SignInRequest;
import etu.nic.store.model.dto.SignUpRequest;
import etu.nic.store.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideSignUpTestData")
    void testRegister(SignUpRequest signUpRequest, int expectedStatus) throws Exception {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("mocked-token");

        if (expectedStatus == 201) {
            when(authService.register(any(SignUpRequest.class))).thenReturn(response);
        } else {
            when(authService.register(any(SignUpRequest.class))).thenThrow(new BadRequestException("Registration failed"));
        }

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @MethodSource("provideSignInTestData")
    void testLogin(SignInRequest signInRequest, int expectedStatus) throws Exception {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("mocked-token");

        if (expectedStatus == 200) {
            when(authService.login(any(SignInRequest.class))).thenReturn(response);
        } else {
            when(authService.login(any(SignInRequest.class))).thenThrow(new BadRequestException("Login failed"));
        }

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is(expectedStatus));
    }

    private Stream<Object[]> provideSignUpTestData() {
        return Stream.of(
                new Object[]{new SignUpRequest("test1@test.com", "John", "Doe", "password", "password"), 201},
                new Object[]{new SignUpRequest("", "John", "Doe", "password", "password"), 400},
                new Object[]{new SignUpRequest("test2@test.com", "", "Doe", "password", "password"), 400},
                new Object[]{new SignUpRequest("test3@test.com", "John", "Doe", "password", "mismatch"), 400}
        );
    }

    private Stream<Object[]> provideSignInTestData() {
        return Stream.of(
                new Object[]{new SignInRequest("test1@test.com", "password"), 200},
                new Object[]{new SignInRequest("invalid@test.com", "wrongPassword"), 400},
                new Object[]{new SignInRequest("", "password"), 400},
                new Object[]{new SignInRequest("test2@test.com", ""), 400}
        );
    }
}
