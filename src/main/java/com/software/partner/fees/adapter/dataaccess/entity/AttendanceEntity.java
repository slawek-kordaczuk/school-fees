package com.software.partner.fees.adapter.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
@Entity
public class AttendanceEntity {

    @Id
    private UUID id;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_id")
    private ChildEntity child;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceEntity that = (AttendanceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
