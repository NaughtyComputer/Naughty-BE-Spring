package naughty.tuzamate.domain.annuity.controller;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.annuity.service.command.AnnuityCommandService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnnuityController {

    private final AnnuityCommandService annuityCommandService;

    @PostMapping("/api/annuity/sync")
    public CustomResponse<?> syncAnnuityProductFromApi() {
        return annuityCommandService.fetchAndSaveAnnuityProducts();
    }
}