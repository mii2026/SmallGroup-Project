package com.server.smallgroup.DTO;

import com.server.smallgroup.Entity.UserHobby;
import com.server.smallgroup.Entity.Users;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class UserInfoDTO {
    private String id;
    private String name;
    private Boolean gender;
    private String phonenum;
    private Integer age;
    private String message;
    private String hobby;
    private String address;

    public UserInfoDTO(Users u, List<UserHobby> list){
        this.id = u.getUserId();
        this.name = u.getName();
        this.gender = u.getGender();
        this.phonenum = u.getPhoneNum();
        this.age = u.getAge();
        this.message = u.getMessage();
        this.address = u.getAddress();

        StringBuilder sb = new StringBuilder("000000000000");
        for(int i = 0; i < list.size(); i++){
            sb.setCharAt(list.get(i).getHobby().getId()-1, '1');
        }
        this.hobby = sb.toString();
    }
}