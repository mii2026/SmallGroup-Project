package com.server.smallgroup.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class FilterInfoDTO {
    private String order;
    private Integer pagenum=0;
    private Integer minage=0;
    private Integer maxage=100;
    private String hobby;
    private Boolean location=false;
}
