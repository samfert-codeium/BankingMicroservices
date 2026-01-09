package org.training.transactions.exception;

/**
 * Exception thrown when an account has insufficient balance for a transaction.
 * 
 * <p>This exception is thrown when a withdrawal or transfer cannot be completed
 * because the account does not have enough available balance.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class InsufficientBalance extends GlobalException {

    /**
     * Constructs an InsufficientBalance exception.
     * 
     * @param message the error message describing the insufficient balance
     */
    public InsufficientBalance(String message) {
        super(message, GlobalErrorCode.BAD_REQUEST);
    }
}
