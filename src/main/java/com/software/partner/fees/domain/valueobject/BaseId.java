package com.software.partner.fees.domain.valueobject;

import java.util.Objects;

public abstract class BaseId <ID> {
    private final ID value;

    protected BaseId(ID value) {
        this.value = value;
    }

    public ID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
