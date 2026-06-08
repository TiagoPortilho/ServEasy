package com.tiagoportilho.ServEasy.dto.response;

import com.tiagoportilho.ServEasy.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String redirectUrl;
    private User.UserRole role;
    private String username;
    private String token;
}
