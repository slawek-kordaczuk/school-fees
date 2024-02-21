package com.software.partner.fees.domain.entity;

import com.software.partner.fees.domain.valueobject.*;
import lombok.Getter;

@Getter
public class School extends Entity<SchoolId>{

    private final Name name;
    private final HourPrice hourPrice;

    public static Builder builder() {
        return new Builder();
    }

    public School(Builder builder) {
        super.setId(builder.schoolId);
        this.name = builder.name;
        this.hourPrice = builder.hourPrice;
    }

    public static final class Builder {
        private SchoolId schoolId;
        private Name name;
        private HourPrice hourPrice;


        public Builder schoolId(SchoolId val) {
            schoolId = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder hourPrice(HourPrice val) {
            hourPrice = val;
            return this;
        }

        public School build() {
            return new School(this);
        }
    }
}
