package com.ddmu.journal.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "log")
public class Log {

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "date")),
            @AttributeOverride(name = "time", column = @Column(name = "time"))
            })
    public DateTime logDateTime;


    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "old_journal_id", nullable = true)
    public Journal oldJournal;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "new_journal_id", nullable = true)
    public Journal newJournal;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "action_id", nullable = false)
    public Action action;

    public Log(User user, DateTime logDateTime,  Journal newJournal,  Action action) {
        this.user = user;
        this.logDateTime = logDateTime;
        this.newJournal = newJournal;
        this.action = action;
    }

    public Log(User user, DateTime logDateTime, Journal oldJournal, Journal newJournal,  Action action) {
        this.user = user;
        this.logDateTime = logDateTime;
        this.oldJournal = oldJournal;
        this.newJournal = newJournal;
        this.action = action;
    }
}
