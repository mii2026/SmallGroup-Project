package com.server.smallgroup.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class FindIdDTO {
    @NotNull(message = "이름을 입력하세요.")
    String name;

    @NotNull(message = "전화번호를 입력하세요.")
    String phonenum;
}
