package naughty.tuzamate.domain.annuity.service.command;

import naughty.tuzamate.global.apiPayload.CustomResponse;

public interface AnnuityCommandService {
    CustomResponse<?> fetchAndSaveAnnuityProducts();
}
