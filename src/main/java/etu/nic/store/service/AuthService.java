package etu.nic.store.service;

import etu.nic.store.model.dto.JwtAuthenticationResponse;
import etu.nic.store.model.dto.SignInRequest;
import etu.nic.store.model.dto.SignUpRequest;

public interface AuthService {
    JwtAuthenticationResponse login(SignInRequest signInRequest);

    JwtAuthenticationResponse register(SignUpRequest signUpRequest);
}
