package com.server.smallgroup.DTO;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.Keyword;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class FindGroupDTO {
    private Long id;
    private String name;
    private Integer minage;
    private Integer maxage;
    private Integer admit;
    private Integer numperson;
    private String keyword;

    public FindGroupDTO(Groups g){
        this.id = g.getId();
        this.name = g.getName();
        this.minage = g.getMinAge();
        this.maxage = g.getMaxAge();
        this.admit = g.getAdmit();
        this.numperson = g.getNumPerson();

        StringBuilder sb = new StringBuilder();
        for(Keyword k: g.getKeyword()){
            sb.append("#"+k.getKeyword());
        }
        this.keyword = sb.toString();
    }
}
