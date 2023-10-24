package com.server.smallgroup.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Groups {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer minAge;
    private Integer maxAge;
    private Integer admit;
    private Integer numPerson;
    private String message;
    private String address;
    private String imagePath;
    private LocalDateTime createTime;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<Keyword> keyword;

    @Builder
    public Groups(String name, Integer minAge, Integer maxAge, Integer admit, Integer numPerson,
                  String address, String message, LocalDateTime createTime){
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.admit = admit;
        this.numPerson = numPerson;
        this.address = address;
        this.message = message;
        this.createTime = createTime;
    }

    public void setContributes(String name, Integer minAge, Integer maxAge, Integer admit,
                               String address, String message){
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.admit = admit;
        this.address = address;
        this.message = message;
    }
}
