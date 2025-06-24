package naughty.tuzamate.domain.deposit.service.command;

import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.BaseSuccessCode;

public interface DepositCommandService {
    CustomResponse<?> fetchAndSaveDepositProducts();
}
