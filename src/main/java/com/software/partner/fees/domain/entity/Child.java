package com.software.partner.fees.domain.entity;

import com.software.partner.fees.domain.valueobject.ChildId;
import com.software.partner.fees.domain.valueobject.FirstName;
import com.software.partner.fees.domain.valueobject.LastName;
import lombok.Getter;

import java.util.List;

@Getter
public class Child extends Entity<ChildId> {

    private final School school;
    private final Parent parent;
    private final FirstName firstName;
    private final LastName lastName;
    private final List<Attendance> attendance;

    public static Builder builder() {
        return new Builder();
    }

    public Child(Builder builder) {
        super.setId(builder.childId);
        this.parent = builder.parent;
        this.school = builder.school;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.attendance = builder.attendance;
    }

    public static final class Builder {
        private ChildId childId;
        private Parent parent;
        private School school;
        private FirstName firstName;
        private LastName lastName;
        private List<Attendance> attendance;


        public Builder childId(ChildId val) {
            childId = val;
            return this;
        }

        public Builder parent(Parent val) {
            parent = val;
            return this;
        }

        public Builder school(School val) {
            school = val;
            return this;
        }

        public Builder firstName(FirstName val) {
            firstName = val;
            return this;
        }

        public Builder lastName(LastName val) {
            lastName = val;
            return this;
        }

        public Builder attendance(List<Attendance> val) {
            attendance = val;
            return this;
        }

        public Child build() {
            return new Child(this);
        }
    }

}
