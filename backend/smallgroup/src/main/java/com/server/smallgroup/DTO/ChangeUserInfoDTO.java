package com.server.smallgroup.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChangeUserInfoDTO {
    private String message;
    @Size(min = 12, max = 12, message = "취미 형식이 맞지 않습니다.")
    @Pattern(regexp = "^[01]*$", message = "모임 이름은 영어와 한글만 가능합니다.")
    private String hobby;
    private String address;

    private String oldpw;
    @Size(min = 7, max = 15, message = "비밀번호가 7-15자리가 아닙니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*?]*$",message = "비밀번호는 영문, 숫자, !@#$%^&*?만 사용 가능합니다.")
    private String newpw;
}
