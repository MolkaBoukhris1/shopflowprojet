package com.shopflow.shpflow.dto.response;
import com.shopflow.shpflow.entity.User;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;

    public static AuthResponse of(String accessToken, String refreshToken, User user) {
        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(UserResponse.fromEntity(user)).build();
    }
}