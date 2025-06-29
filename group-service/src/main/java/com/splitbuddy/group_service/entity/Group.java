package com.splitbuddy.group_service.entity;
import com.splitbuddy.group_service.entity.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "user_groups", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

     @ManyToMany
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
     @JsonIgnoreProperties({"groups"})
    private Set<AppUser> members = new HashSet<>();
}
