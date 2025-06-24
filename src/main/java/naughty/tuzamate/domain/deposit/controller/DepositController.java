package naughty.tuzamate.domain.deposit.controller;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.deposit.service.command.DepositCommandService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DepositController {

    private final DepositCommandService depositCommandService;

    @PostMapping("/api/deposit/sync")
    public CustomResponse<?> syncDepositProductFromApi() {
        return depositCommandService.fetchAndSaveDepositProducts();
    }
}