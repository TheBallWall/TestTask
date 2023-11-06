package org.example.entities;

import java.util.HashSet;
import java.util.Objects;

public class ValuesHashSet<BigInteger> extends HashSet<BigInteger> {


    public boolean equalsByAnyValue(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        ValuesHashSet<BigInteger> other = (ValuesHashSet<BigInteger>) o;
        if (other.size() == 0 || this.size() == 0) return false;
        if (other.size() > this.size()) {
            for (BigInteger value : this) if (value != null && other.contains(value)){
                return true;
            }
        } else {
            for (BigInteger value : other) if (value != null && this.contains(value)){
                return true;
            }
        }
        return false;
    }
}
