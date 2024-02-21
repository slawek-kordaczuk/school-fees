package com.software.partner.fees.domain.entity;

import com.software.partner.fees.domain.valueobject.FirstName;
import com.software.partner.fees.domain.valueobject.LastName;
import com.software.partner.fees.domain.valueobject.ParentId;
import lombok.Getter;

@Getter
public class Parent extends Entity<ParentId>{

    private final FirstName firstName;
    private final LastName lastName;

    public static Builder builder() {
        return new Builder();
    }

    public Parent(Builder builder) {
        super.setId(builder.parentId);
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static final class Builder {
        private ParentId parentId;
        private FirstName firstName;
        private LastName lastName;


        public Builder parentId(ParentId val) {
            parentId = val;
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

        public Parent build() {
            return new Parent(this);
        }
    }

}
