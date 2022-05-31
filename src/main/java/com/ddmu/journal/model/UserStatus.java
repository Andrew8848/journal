package com.ddmu.journal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_status")
public class UserStatus {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "value", nullable = false, length = 16, unique = true)
    private String value;

//    @OneToMany(mappedBy = "userStatus")
//    @JsonIgnore
//    private User user;
}
