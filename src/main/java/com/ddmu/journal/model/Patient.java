package com.ddmu.journal.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patient")
public class Patient {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name")),
            @AttributeOverride(name = "surname", column = @Column(name = "surname"))
    })
    private NameSurname nameSurname;

    @NonNull
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

}
