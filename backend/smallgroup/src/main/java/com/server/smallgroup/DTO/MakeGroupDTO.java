package com.server.smallgroup.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class MakeGroupDTO {
    Long id;

    @NotBlank(message = "모임 이름을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣 ]*$", message = "모임 이름은 영어와 한글만 가능합니다.")
    String name;

    @NotNull(message = "취미는 빌 수 없습니다.") @Size(min = 12, max = 12, message = "취미 형식이 맞지 않습니다.")
    String hobby;

    @NotNull(message = "최소 나이를 입력하세요.")
    @Min(value = 1, message = "최소 나이의 최소값은 1입니다.") @Max(value = 100, message = "최대값은 100입니다.")
    Integer minage;

    @NotNull(message = "최대 나이를 입력하세요.")
    @Min(value = 1, message = "최대 나이의 최소값은 1입니다.") @Max(value = 100, message = "최대값은 100입니다.")
    Integer maxage;

    @NotNull(message = "정원이 없습니다.") @Min(value = 1, message = "정원의 최소값은 1입니다.")
    Integer admit;

    @NotBlank(message = "모임 소개를 입력하세요.")
    String message;

    @NotBlank(message = "주소를 입력하세요")
    String address;

    @NotBlank(message = "모임 키워드를 입력하세요.")
    String keyword;
}
