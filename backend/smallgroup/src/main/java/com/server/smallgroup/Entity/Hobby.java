package com.server.smallgroup.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hobby {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hobby;

    public Hobby(String hobby){
        this.hobby = hobby;
    }
}
