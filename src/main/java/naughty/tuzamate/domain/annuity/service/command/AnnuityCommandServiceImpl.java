package naughty.tuzamate.domain.annuity.service.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.annuity.converter.AnnuityConverter;
import naughty.tuzamate.domain.annuity.dto.AnnuityRequestDTO;
import naughty.tuzamate.domain.annuity.repository.AnnuityRepository;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnuityCommandServiceImpl implements AnnuityCommandService {

    private final AnnuityRepository annuityRepository;
    private final ObjectMapper objectMapper;

    @Value("${openapi.key}")
    private String openApiKey;

    public CustomResponse<?> fetchAndSaveAnnuityProducts() {
        String API_URL = "https://finlife.fss.or.kr/finlifeapi/annuitySavingProductsSearch.json"
                + "?auth="
                + openApiKey
                + "&topFinGrpNo=060000&pageNo=1";

        try {
            RestTemplate restTemplate = new RestTemplate();

            String jsonResponse = restTemplate.getForObject(API_URL, String.class);

            AnnuityRequestDTO.ApiResultDTO apiResponse = objectMapper
                    .readValue(jsonResponse, AnnuityRequestDTO.ApiResultDTO.class);

            for (AnnuityRequestDTO.AnnuityProductDTO dto : apiResponse.result().baseList()) {
                annuityRepository.save(AnnuityConverter.toEntity(dto));
            }

            return CustomResponse.onSuccess(GeneralSuccessCode.OK);

        } catch (Exception e) {
            e.printStackTrace(); // 또는 로그
        }

        return CustomResponse.onFail(GeneralErrorCode.UNAUTHORIZED_401);
    }
}
