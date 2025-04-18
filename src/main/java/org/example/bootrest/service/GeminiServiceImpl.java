package org.example.bootrest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // JSON 직렬화/역직렬화를 위한 Jackson 도구
import lombok.extern.java.Log; // 로그 출력을 위한 롬복 어노테이션
import org.example.bootrest.model.dto.AnimalRequestDTO;
import org.example.bootrest.model.dto.GeminiRequestDTO;
import org.example.bootrest.model.dto.GeminiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient; // Java 11부터 내장된 HTTP 클라이언트
import java.net.http.HttpRequest; // 요청 생성용
import java.net.http.HttpResponse; // 응답 처리용
import java.util.List;

@Service // Spring이 이 클래스를 서비스 빈으로 등록하도록 지정
@Log // 로그 출력을 위한 Lombok 어노테이션
public class GeminiServiceImpl implements GeminiService {

    @Value("${gemini.api-key}") // application.properties에서 gemini.api-key 값을 주입
    private String apiKey;

    @Override
    public String makeStory(AnimalRequestDTO dto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(); // JSON <-> 객체 변환용 Jackson 도구 생성
        HttpClient client = HttpClient.newHttpClient(); // HTTP 요청을 보낼 클라이언트 객체 생성

        // Gemini API 호출 URL (API 키 포함)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=%s"
                .formatted(apiKey);

        // 사용자 입력값으로부터 prompt 문자열 생성
        String prompt = "%s를 이름으로 %s를 설명으로 하는 생명체에 대한 이야기를 작성해줘. 이야기만 평문 및 한글로 간결하게 200자 이내로 작성해줘."
                .formatted(dto.name(), dto.description());

        // 요청 본문에 넣을 JSON 데이터를 GeminiRequestDTO로 구성 → JSON 문자열로 변환
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) // 요청 보낼 주소 설정
                .header("Content-Type", "application/json") // 헤더 설정
                .POST(HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString( // 객체를 JSON 문자열로 변환
                                new GeminiRequestDTO( // 최상위 요청 객체
                                        List.of(
                                            new GeminiRequestDTO.Content( // Content 리스트
                                                List.of(
                                                    new GeminiRequestDTO.Part(prompt) // Part 리스트에 prompt 담기
                                                )
                                            )
                                        )
                                )
                        )
                ))
                .build(); // 최종 요청 객체 생성

        // 위에서 만든 요청을 실제로 서버에 보내고, 응답을 문자열로 받음
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 받은 응답 내용을 로그로 출력 (디버깅용)
        log.info(response.body());

        // 응답 JSON 문자열을 GeminiResponseDTO 객체로 역직렬화 (파싱)
        GeminiResponseDTO resp = objectMapper.readValue(response.body(), GeminiResponseDTO.class);

        // 응답 구조: candidates[0] → content → parts[0] → text 추출
        return resp.candidates().get(0).content().parts().get(0).text();
    }
}
