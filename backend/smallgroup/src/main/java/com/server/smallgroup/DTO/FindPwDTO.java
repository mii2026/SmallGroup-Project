package com.server.smallgroup.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class FindPwDTO {
    @NotNull(message = "이름을 입력하세요.")
    String name;

    @NotNull(message = "아이디를 입력하세요.")
    String id;

    @Size(min = 10, max = 11, message = "전화번호 형식이 맞지 않습니다.")
    @Pattern(regexp = "^[0-9]*$", message = "전화번호 형식이 맞지 않습니다.") @NotNull(message = "전화번호를 입력하세요.")
    String phonenum;
}
