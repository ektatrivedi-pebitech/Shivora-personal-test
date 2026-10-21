
package com.shivora.auth.service;

import com.shivora.auth.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    String login(String email, String password);
}
