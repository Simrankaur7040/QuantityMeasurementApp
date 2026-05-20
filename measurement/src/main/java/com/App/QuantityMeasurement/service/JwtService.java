package com.App.QuantityMeasurement.service;
public interface JwtService {
    String generateToken(String email, String name, String picture);
    String extractUsername(String token);
    boolean isTokenValid(String token);
}
