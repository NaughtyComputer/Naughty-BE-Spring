package naughty.tuzamate.domain.savings.service.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.savings.converter.SavingsConverter;
import naughty.tuzamate.domain.savings.dto.SavingsRequestDTO;
import naughty.tuzamate.domain.savings.repository.SavingsRepository;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class SavingsCommandServiceImpl implements SavingsCommandService {

    private final SavingsRepository savingsRepository;
    private final ObjectMapper objectMapper;

    @Value("${openapi.key}")
    private String openApiKey;

    public CustomResponse<?> fetchAndSaveSavingsProducts() {
        String API_URL = "https://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json"
                + "?auth="
                + openApiKey
                + "&topFinGrpNo=020000&pageNo=1";

        try {
            RestTemplate restTemplate = new RestTemplate();

            String jsonResponse = restTemplate.getForObject(API_URL, String.class);

            SavingsRequestDTO.ApiResultDTO apiResponse = objectMapper
                    .readValue(jsonResponse, SavingsRequestDTO.ApiResultDTO.class);

            for (SavingsRequestDTO.SavingsProductDTO dto : apiResponse.result().baseList()) {
                savingsRepository.save(SavingsConverter.toEntity(dto));
            }

            return CustomResponse.onSuccess(GeneralSuccessCode.OK);

        } catch (Exception e) {
            e.printStackTrace(); // 또는 로그
        }

        return CustomResponse.onFail(GeneralErrorCode.UNAUTHORIZED_401);
    }
}