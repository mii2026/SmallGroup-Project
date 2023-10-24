package com.server.smallgroup.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 15)
    private String userId;
    @Column(length = 15)
    private String pw;
    @Column(length = 11)
    private String phoneNum;
    private String name;
    private Boolean gender;
    private Integer age;
    private String address;
    private String photoPath;
    private String message;

    @Builder
    public Users(String userId, String pw, String phoneNum, String name, Boolean gender, Integer age, String address){
        this.userId = userId;
        this.pw = pw;
        this.phoneNum = phoneNum;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
    }

    public void setPw(String pw){
        this.pw = pw;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }
}
