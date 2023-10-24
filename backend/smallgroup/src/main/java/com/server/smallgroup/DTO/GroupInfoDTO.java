package com.server.smallgroup.DTO;

import com.server.smallgroup.Entity.GroupHobby;
import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.Users;
import lombok.Data;

import java.util.List;

@Data
public class GroupInfoDTO {
    private Long id;
    private String name;
    private String message;
    private String address;
    private Integer minAge;
    private Integer maxAge;
    private Integer admin;
    private Integer numPerson;

    private String hobby;
    private String keyword;
    private String users;

    public GroupInfoDTO(Groups g, List<GroupHobby> hobby_list, List<Users> user_list){
        this.id = g.getId();
        this.name = g.getName();
        this.message = g.getMessage();
        this.address = g.getAddress();
        this.minAge = g.getMinAge();
        this.maxAge = g.getMaxAge();
        this.admin = g.getAdmit();
        this.numPerson = g.getNumPerson();

        StringBuilder sb = new StringBuilder("000000000000");
        for(int i = 0; i < hobby_list.size(); i++){
            sb.setCharAt(hobby_list.get(i).getHobby().getId()-1, '1');
        }
        this.hobby = sb.toString();

        sb = new StringBuilder();
        for(int i = 0; i < g.getKeyword().size(); i++){
            sb.append("#"+g.getKeyword().get(i).getKeyword());
        }
        this.keyword = sb.toString();

        sb = new StringBuilder();
        sb.append(user_list.get(0).getId());
        for(int i = 1; i < user_list.size(); i++){
            sb.append(", "+user_list.get(i).getId());
        }
        this.users = sb.toString();
    }
}
