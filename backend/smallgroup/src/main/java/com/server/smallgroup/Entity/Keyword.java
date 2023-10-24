package com.server.smallgroup.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="group_id")
    private Groups group;
    private String keyword;

    @Builder
    public Keyword(Groups group, String keyword){
        this.group = group;
        this.keyword = keyword;
    }
}
