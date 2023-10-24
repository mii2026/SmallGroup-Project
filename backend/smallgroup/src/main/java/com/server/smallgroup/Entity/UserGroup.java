package com.server.smallgroup.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="group_id")
    private Groups group;
    private Boolean isCaptain;
    private LocalDateTime registerTime;

    @Builder
    public UserGroup(Users user, Groups group, Boolean isCaptain, LocalDateTime registerTime){
        this.user = user;
        this.group = group;
        this.isCaptain = isCaptain;
        this.registerTime = registerTime;
    }
}
