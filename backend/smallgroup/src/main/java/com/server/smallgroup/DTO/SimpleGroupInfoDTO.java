package com.server.smallgroup.DTO;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.Keyword;
import com.server.smallgroup.Entity.UserGroup;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class SimpleGroupInfoDTO {
    private Long id;
    private String name;
    private boolean iscaptain;

    public SimpleGroupInfoDTO(UserGroup ug){
        this.id = ug.getGroup().getId();
        this.name = ug.getGroup().getName();
        this.iscaptain = ug.getIsCaptain();
    }
}
