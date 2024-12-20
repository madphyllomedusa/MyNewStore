package etu.nic.store.controller;

import etu.nic.store.model.dto.JwtAuthenticationResponse;
import etu.nic.store.model.dto.SignInRequest;
import etu.nic.store.model.dto.SignUpRequest;
import etu.nic.store.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        JwtAuthenticationResponse jwtAuthenticationResponse = authService.register(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jwtAuthenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody SignInRequest signInRequest) {
        JwtAuthenticationResponse jwtAuthenticationResponse = authService.login(signInRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwtAuthenticationResponse);
    }
}
