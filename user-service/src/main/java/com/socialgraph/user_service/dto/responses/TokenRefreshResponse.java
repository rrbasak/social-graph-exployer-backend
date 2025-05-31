package com.socialgraph.user_service.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {
	private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
