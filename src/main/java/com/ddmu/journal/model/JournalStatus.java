package com.ddmu.journal.model;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "journal_status")
public class JournalStatus {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "value", nullable = false, length = 32, unique = true)
    private String value;

}
