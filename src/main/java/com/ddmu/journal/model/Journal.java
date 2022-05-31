package com.ddmu.journal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "journal")
public class Journal {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "date_publication")),
            @AttributeOverride(name = "time", column = @Column(name = "time_publication"))
    })
    private DateTime dateTimePublication;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "date_last_modified")),
            @AttributeOverride(name = "time", column = @Column(name = "time_last_modified"))
    })
    private DateTime dateTimeLastModified;

//    @Column(name = "consultant")
//    @Enumerated(EnumType.STRING)
//    private Consultant consultant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @Column(name = "diagnosis", nullable = false, length = 2056)
    private String diagnosis;

//    @Column(name = "status")
//    @Enumerated(EnumType.STRING)
//    private JournalStatus status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "journal_status_id", nullable = false)
    private JournalStatus journalStatus;

}
