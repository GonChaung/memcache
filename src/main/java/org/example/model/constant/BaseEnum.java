package org.example.model.constant;

public interface BaseEnum<V> {
    V getValue();

    static <E extends Enum<E> & BaseEnum<?>> E fromValue(Class<E> enumClass, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        for (E type : enumClass.getEnumConstants()) {
            if (type.getValue().toString().equals(value.toString())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
