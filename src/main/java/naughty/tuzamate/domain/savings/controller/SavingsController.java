package naughty.tuzamate.domain.savings.controller;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.savings.service.command.SavingsCommandService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsCommandService savingsCommandService;

    @PostMapping("/api/savings/sync")
    public CustomResponse<?> syncSavingsProductFromApi() {
        return savingsCommandService.fetchAndSaveSavingsProducts();
    }
}