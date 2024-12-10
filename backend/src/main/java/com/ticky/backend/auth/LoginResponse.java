package com.ticky.backend.auth;

import com.ticky.backend.dto.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Response {
    private String token;
    private String message;
}
