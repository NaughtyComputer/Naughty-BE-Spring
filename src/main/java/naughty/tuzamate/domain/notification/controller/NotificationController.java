package naughty.tuzamate.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.notification.dto.NotificationResDTO;
import naughty.tuzamate.domain.notification.service.NotificationService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "알림 컨트롤러", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    @Operation(summary = "알림 목록 조회",
            description = "알림 표시를 누르면 알림 목록 리스트가 조회가 됩니다.")
    public CustomResponse<NotificationResDTO.NotificationCursorResDTO> getNotificationList(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "15") int size
    ) {
        NotificationResDTO.NotificationCursorResDTO notificationList =
                notificationService.getNotificationList(principal.getId(), cursor, size);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, notificationList);
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = "단일 알림 조회",
            description = "이미 읽은 알림 단일 조회")
    public CustomResponse<NotificationResDTO.toNotificationResDTO> getNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        NotificationResDTO.toNotificationResDTO notification =
                notificationService.getNotification(notificationId, principal.getId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, notification);
    }

    @PatchMapping("/{notificationId}")
    @Operation(summary = "단일 알림 조회 및 읽음 처리",
            description = "아직 읽지 않은 알림을 읽음 처리하고 조회")
    public CustomResponse<NotificationResDTO.toNotificationResDTO> updateIsRead(@PathVariable Long notificationId,
                                          @AuthenticationPrincipal PrincipalDetails principal
    ) {
        NotificationResDTO.toNotificationResDTO notification =
                notificationService.updateIsRead(notificationId, principal.getId());

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, notification);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제",
            description = "단일 알림 삭제")
    public CustomResponse<?> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal PrincipalDetails principal) {

        notificationService.deleteNotification(notificationId, principal.getId());

        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }
}
