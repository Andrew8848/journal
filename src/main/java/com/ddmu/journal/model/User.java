package com.ddmu.journal.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "date_creation")),
            @AttributeOverride(name = "time", column = @Column(name = "time_creation"))
    })
    private DateTime dateTimeCreation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "date_was_online")),
            @AttributeOverride(name = "time", column = @Column(name = "time_was_online"))
    })
    private DateTime dateTimeWasOnline;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_status_id", nullable = false)
    private UserStatus userStatus;

}
