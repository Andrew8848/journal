package com.ddmu.journal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class NameSurname {
    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 128, nullable = false)
    private String surname;
}
