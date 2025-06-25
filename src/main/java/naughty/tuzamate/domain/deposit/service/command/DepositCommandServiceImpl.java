package naughty.tuzamate.domain.deposit.service.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.deposit.converter.DepositConverter;
import naughty.tuzamate.domain.deposit.dto.DepositRequestDTO;
import naughty.tuzamate.domain.deposit.repository.DepositRepository;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.success.BaseSuccessCode;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositCommandServiceImpl implements DepositCommandService {

    private final DepositRepository depositRepository;
    private final ObjectMapper objectMapper;

    @Value("${openapi.key}")
    private String openApiKey;

    public CustomResponse<?> fetchAndSaveDepositProducts() {
        String API_URL = "https://finlife.fss.or.kr/finlifeapi/depositProductsSearch.json"
                        + "?auth="
                        + openApiKey
                        + "&topFinGrpNo=020000&pageNo=1";

        try {
            // Spring 에서 제공하는 HTTP 요청을 위한 객체
            // webClient로 바뀌는 추세이니 나중에 시도할 것
            RestTemplate restTemplate = new RestTemplate();

            //지정한 API URL로 HTTP GET 요청을 보내고, 그 응답을 문자열(String)로 받음
            String jsonResponse = restTemplate.getForObject(API_URL, String.class);

            // ObjectMapper 는 Jackson 라이브러리의 JSON 파서
            // 응답 JSON 문자열을 DepositRequestDTO.ApiResultDTO 클래스 구조에 맞춰 객체로 역직렬화(deserialize)
            DepositRequestDTO.ApiResultDTO apiResponse = objectMapper
                    .readValue(jsonResponse, DepositRequestDTO.ApiResultDTO.class);

            for (DepositRequestDTO.DepositProductDTO dto : apiResponse.result().baseList()) {
                depositRepository.save(DepositConverter.toEntity(dto));
            }

            return CustomResponse.onSuccess(GeneralSuccessCode.OK);

        } catch (Exception e) {
            e.printStackTrace(); // 또는 로그
        }

        return CustomResponse.onFail(GeneralErrorCode.UNAUTHORIZED_401);
    }
}

