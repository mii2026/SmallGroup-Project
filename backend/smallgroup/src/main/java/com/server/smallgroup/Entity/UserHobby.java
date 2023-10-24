package com.server.smallgroup.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHobby {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="hobby_id")
    private Hobby hobby;

    @Builder
    public UserHobby(Users user, Hobby hobby){
        this.user = user;
        this.hobby = hobby;
    }
}
