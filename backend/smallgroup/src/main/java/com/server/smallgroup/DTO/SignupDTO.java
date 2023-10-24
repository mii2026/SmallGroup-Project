package com.server.smallgroup.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SignupDTO {
    @NotNull(message = "이름을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "이름은 영어와 한글만 가능합니다.")
    private String name;

    @NotNull(message = "성별을 입력하세요.")
    private Boolean gender;

    @NotNull(message = "나이를 입력하세요.")
    private Integer age;

    @Size(min = 7, max = 15, message = "아이디가 7-15자리가 아닙니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "아이디는 영문, 숫자, -_만 사용 가능합니다.") @NotNull(message = "아이디를 입력하세요.")
    private String ID;

    @Size(min = 7, max = 15, message = "비밀번호가 7-15자리가 아닙니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*?]*$",message = "비밀번호는 영문, 숫자, !@#$%^&*?만 사용 가능합니다.") @NotNull(message = "비밀번호를 입력하세요.")
    private String PW;

    @Size(min = 10, max = 11, message = "전화번호 형식이 맞지 않습니다.")
    @Pattern(regexp = "^[0-9]*$", message = "전화번호 형식이 맞지 않습니다.") @NotNull(message = "전화번호를 입력하세요.")
    private String phonenum;

    @NotNull(message = "주소를 입력하세요.")
    private String address;
}
