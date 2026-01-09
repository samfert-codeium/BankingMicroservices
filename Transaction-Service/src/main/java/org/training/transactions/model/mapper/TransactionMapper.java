package org.training.transactions.model.mapper;

import org.springframework.beans.BeanUtils;
import org.training.transactions.model.dto.TransactionDto;
import org.training.transactions.model.entity.Transaction;

import java.util.Objects;

/**
 * Mapper class for converting between {@link Transaction} entities and {@link TransactionDto} objects.
 * 
 * <p>This mapper extends {@link BaseMapper} and provides specific implementation
 * for converting transaction data between the persistence layer (entities) and
 * the API layer (DTOs).</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.transactions.model.mapper.BaseMapper
 */
public class TransactionMapper extends BaseMapper<Transaction, TransactionDto> {

    /**
     * Converts a {@link TransactionDto} to a {@link Transaction} entity.
     * 
     * @param dto the TransactionDto to convert
     * @param args optional additional arguments (not used)
     * @return the converted Transaction entity
     */
    @Override
    public Transaction convertToEntity(TransactionDto dto, Object... args) {

        Transaction transaction = new Transaction();
        if(!Objects.isNull(dto)){
            BeanUtils.copyProperties(dto, transaction);
        }
        return transaction;
    }

    /**
     * Converts a {@link Transaction} entity to a {@link TransactionDto}.
     * 
     * @param entity the Transaction entity to convert
     * @param args optional additional arguments (not used)
     * @return the converted TransactionDto
     */
    @Override
    public TransactionDto convertToDto(Transaction entity, Object... args) {

        TransactionDto transactionDto = new TransactionDto();
        if(!Objects.isNull(entity)) {
            BeanUtils.copyProperties(entity, transactionDto);
        }
        return transactionDto;
    }
}
