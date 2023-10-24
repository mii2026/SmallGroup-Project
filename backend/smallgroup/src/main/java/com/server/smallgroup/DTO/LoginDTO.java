package com.server.smallgroup.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginDTO {
    @NotNull(message = "아이디를 입력하세요.")
    private String id;

    @NotNull(message = "비밀번호를 입력하세요.")
    private String pw;
}
