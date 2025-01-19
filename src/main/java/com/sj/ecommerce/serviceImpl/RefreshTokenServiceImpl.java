package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.entity.RefreshToken;
import com.sj.ecommerce.repository.RefreshTokenRepository;
import com.sj.ecommerce.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7)); // Set token validity to 7 days

        return refreshTokenRepository.save(refreshToken);
    }
     
    @Override
    public Optional<RefreshToken> validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public void deleteAllTokensForUser(String userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
