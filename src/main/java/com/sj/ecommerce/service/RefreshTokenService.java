package com.sj.ecommerce.service;

import com.sj.ecommerce.entity.RefreshToken;

import java.util.Optional;

public interface  RefreshTokenService {


    public RefreshToken createRefreshToken(String userId);

    public Optional<RefreshToken> validateRefreshToken(String token);

    public void deleteRefreshToken(String token);

    public void deleteAllTokensForUser(String userId);
}
