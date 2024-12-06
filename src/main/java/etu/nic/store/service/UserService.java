package etu.nic.store.service;

public interface UserService {
    Long extractUserIdFromContext();
    Long findUserIdByEmail(String email);
}
