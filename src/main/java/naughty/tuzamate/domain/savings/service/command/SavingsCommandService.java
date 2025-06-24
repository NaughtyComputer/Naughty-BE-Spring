package naughty.tuzamate.domain.savings.service.command;

import naughty.tuzamate.global.apiPayload.CustomResponse;

public interface SavingsCommandService {
    CustomResponse<?> fetchAndSaveSavingsProducts();
}
