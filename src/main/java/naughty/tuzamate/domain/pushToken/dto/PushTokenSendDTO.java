package naughty.tuzamate.domain.pushToken.dto;

import java.util.List;
import java.util.Map;

public class PushTokenSendDTO {

    public record SendToTokenRequest (
        String token,
        String title,
        String body,
        Map<String, String> data,
        Boolean highPriority, // null 허용 → 기본 false
        String clickAction
    ) {}

    public record SendToTokensRequest (
        List<String> tokens,
        String title,
        String body,
        Map<String, String> data,
        Boolean highPriority,
        String clickAction
    ) {}

}