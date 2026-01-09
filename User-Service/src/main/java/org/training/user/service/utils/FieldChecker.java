package org.training.user.service.utils;

import java.util.Arrays;

/**
 * Utility class for checking field values in objects.
 * 
 * <p>This class provides utility methods for validating object fields,
 * particularly useful for checking if required fields are populated
 * before performing certain operations like user approval.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class FieldChecker {

    /**
     * Checks if an object has empty fields.
     * 
     * <p>Recursively checks all declared fields of the object. Returns true if
     * any field is null (except for enum fields). For nested objects, it
     * recursively checks their fields as well.</p>
     *
     * @param object the object to check
     * @return true if the object has empty fields, false otherwise
     */
    public static boolean hasEmptyFields(Object object) {

        if (object == null){
            return true;
        }

        return Arrays.stream(object.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .anyMatch(field -> {
                    try {
                        Object value = field.get(object);

                        if (value != null) {
                            if (field.getType().isEnum()) {
                                return false;
                            } else if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.")) {
                                return hasEmptyFields(value);
                            }
                        }

                        return value == null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

    }
}
