package org.example.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Gender {
    MALE("M"),
    FEMALE("F");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim().toUpperCase();
        switch (value) {
            case "M":
            case "MALE":
                return MALE;
            case "F":
            case "FEMALE":
                return FEMALE;
            default:
                throw new IllegalArgumentException("Unknown gender value: " + value);
        }
    }
}