package naughty.tuzamate.domain.pushToken.service;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.pushToken.code.PushTokenErrorCode;
import naughty.tuzamate.domain.pushToken.dto.PushTokenReqDTO;
import naughty.tuzamate.domain.pushToken.entity.PushToken;
import naughty.tuzamate.domain.pushToken.repository.PushTokenRepository;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PushTokenService {
    private final PushTokenRepository pushTokenRepository;

    // token 이 존재하는 경우 -> update, token 이 존재하지 않는 경우 insert
    @Transactional
    public PushToken upsert(PushTokenReqDTO.RegisterPushTokenReqDTO request, Long userId) {
        PushToken entity = pushTokenRepository.findByToken(request.token())
                // 이미 존재하는 경우 -> update
                .map(exist -> {
                    exist.setUserId(userId);
                    exist.setPlatform(request.platform());
                    exist.setDeviceId(request.deviceId());
                    exist.setIsActive(true);
                    exist.setLastSeenAt(LocalDateTime.now());
                    return exist;
                })
                // 없는 경우 -> insert
                .orElseGet(() -> PushToken.builder()
                        .userId(userId)
                        .token(request.token())
                        .platform(request.platform())
                        .deviceId(request.deviceId())
                        .isActive(true)
                        .lastSeenAt(LocalDateTime.now())
                        .build()
                );

        return pushTokenRepository.save(entity);
    }

    // FCM 토큰을 서버에서 해제
    @Transactional
    public void unbind(String token, boolean deactivate) {
        PushToken pt = pushTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(PushTokenErrorCode.TOKEN_NOT_FOUND));

        pt.setUserId(null);

        if (deactivate) pt.setIsActive(false);

        pt.setLastSeenAt(LocalDateTime.now());
    }

    @Transactional
    public void touch(String token, Long userId) {
        PushToken pt = pushTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(PushTokenErrorCode.TOKEN_NOT_FOUND));

        if (pt.getUserId() == null || !pt.getUserId().equals(userId)) {
            throw new CustomException(PushTokenErrorCode.TOKEN_NOT_OWNER);
        }

        // 앱이 살아있음을 표시
        pt.setIsActive(true);
        pt.setLastSeenAt(LocalDateTime.now());
    }
}
