package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "todo")
public class Todo extends Auditable {

    private String title;
    private String priority;

    @Builder
    public Todo(int id, LocalDateTime createdAt, String title, String priority, AuthUser createdUser) {
        super(id, createdAt);
        this.title = title;
        this.priority = priority;
        this.createdUser = createdUser;
    }

    @ManyToOne
    private AuthUser createdUser;
}
