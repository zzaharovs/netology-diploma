package ru.netology.cloudstorage.entity.db;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserJwtEntity {

    @Id
    private String jwtToken;
    @Column(nullable = false)
    private String username;

}
