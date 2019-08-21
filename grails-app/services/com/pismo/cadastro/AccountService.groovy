package com.pismo.cadastro

import com.pismo.cadastro.Account
import com.pismo.cadastro.AccountCommand
import grails.validation.ValidationException
import org.springframework.validation.FieldError

class AccountService {

    Account update(AccountCommand accountCommand, Account account) {
        account.availableCreditLimit -= accountCommand.available_credit_limit
        account.availableWithdrawalLimit -= accountCommand.available_withdrawal_limit
        account.save(flush:true, failOnError:true)
        account
    }

    Map validation(AccountCommand accountCommand, Boolean throwExcception = false) {
        def errors = [] as Set<FieldError>
        accountCommand.validate()
        errors.addAll(accountCommand.errors.fieldErrors)
        if (throwExcception && errors) {
            throw new ValidationException("Inválido\n${errors}", accountCommand.errors)
        }
        def responseErrors = []
        if (errors) {
            errors.each { fieldError ->
                def error = [:]
                error.code = "VALIDATION"
                error.field = fieldError.field
                error.rejectedValue = fieldError.rejectedValue
                responseErrors << error
            }
        }

        [errors: responseErrors]
    }
}
