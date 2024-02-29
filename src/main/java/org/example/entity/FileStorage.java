package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file_storage")
public class FileStorage extends Auditable {

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "generated_name")
    private String generatedName;

    @Column(name = "content_type")
    private String contentType;

    private long size;

    @OneToOne
    private AuthUser user;

    @Builder
    public FileStorage(int id, LocalDateTime createdAt, String originalName, String generatedName, String contentType, long size, AuthUser user) {
        super(id, createdAt);
        this.originalName = originalName;
        this.generatedName = generatedName;
        this.contentType = contentType;
        this.size = size;
        this.user = user;
    }
}
