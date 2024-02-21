package com.software.partner.fees.adapter.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "child")
@Entity
public class ChildEntity {

    @Id
    private UUID id;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size=100)
    private List<AttendanceEntity> attendance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private SchoolEntity school;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildEntity that = (ChildEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
