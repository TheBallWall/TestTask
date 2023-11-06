package org.example.entities;

import java.util.HashSet;
import java.util.Objects;

public class ValuesHashSet<BigInteger> extends HashSet<BigInteger> {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        ValuesHashSet<BigInteger> other = (ValuesHashSet<BigInteger>) o;
        if (other.size() == 0 || this.size() == 0) return false;
        if (other.size() > this.size()) {
            for (BigInteger value : this) if (other.contains(value)) return true;
        } else {
            for (BigInteger value : other) if (this.contains(value)) return true;
        }
        return false;
    }
}
