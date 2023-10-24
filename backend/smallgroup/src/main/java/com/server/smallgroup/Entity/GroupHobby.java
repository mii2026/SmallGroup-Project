package com.server.smallgroup.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupHobby {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="group_id")
    private Groups group;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="hobby_id")
    private Hobby hobby;

    @Builder
    public GroupHobby(Groups group, Hobby hobby){
        this.group = group;
        this.hobby = hobby;
    }
}
