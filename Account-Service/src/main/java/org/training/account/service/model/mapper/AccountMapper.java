package org.training.account.service.model.mapper;

import org.springframework.beans.BeanUtils;
import org.training.account.service.model.dto.AccountDto;
import org.training.account.service.model.entity.Account;

import java.util.Objects;

/**
 * Mapper class for converting between {@link Account} entities and {@link AccountDto} objects.
 * 
 * <p>This mapper extends {@link BaseMapper} and provides specific implementation
 * for converting account data between the persistence layer (entities) and the
 * API layer (DTOs). It uses Spring's BeanUtils for property copying.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.model.mapper.BaseMapper
 */
public class AccountMapper extends BaseMapper<Account, AccountDto> {

    /**
     * Converts an {@link AccountDto} to an {@link Account} entity.
     * 
     * <p>Uses Spring's BeanUtils to copy properties from the DTO to a new entity.
     * Returns an empty Account if the DTO is null.</p>
     * 
     * @param dto the AccountDto to convert
     * @param args optional additional arguments (not used)
     * @return the converted Account entity
     */
    @Override
    public Account convertToEntity(AccountDto dto, Object... args) {
        Account account = new Account();
        if(!Objects.isNull(dto)){
            BeanUtils.copyProperties(dto, account);
        }
        return account;
    }

    /**
     * Converts an {@link Account} entity to an {@link AccountDto}.
     * 
     * <p>Uses Spring's BeanUtils to copy properties from the entity to a new DTO.
     * Returns an empty AccountDto if the entity is null.</p>
     * 
     * @param entity the Account entity to convert
     * @param args optional additional arguments (not used)
     * @return the converted AccountDto
     */
    @Override
    public AccountDto convertToDto(Account entity, Object... args) {
        AccountDto accountDto = new AccountDto();
        if(!Objects.isNull(entity)) {
            BeanUtils.copyProperties(entity, accountDto);
        }
        return accountDto;
    }
}
