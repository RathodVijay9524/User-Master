package com.vijay.User_Master.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseModel {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean isActive;
    private boolean isDeleted;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY) // Define the inverse relationship
    @JsonBackReference
    private Set<User> users;

    // Constructor with @JsonCreator to create Role from a string
    @JsonCreator
    public Role(@JsonProperty("name") String name) {
        this.name = name;
    }
}