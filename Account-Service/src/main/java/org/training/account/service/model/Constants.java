package org.training.account.service.model;

/**
 * Constants class containing application-wide constant values.
 * 
 * <p>This utility class holds constant values used throughout the Account Service,
 * such as the account number prefix. The class cannot be instantiated.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class Constants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {
    }
    
    /**
     * The prefix used for generating account numbers.
     * All account numbers start with this prefix followed by a sequential number.
     * Example: "0600140000001"
     */
    public static final String ACC_PREFIX = "060014";
}
