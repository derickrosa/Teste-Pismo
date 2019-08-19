package com.pismo.cadastro

import com.pismo.cadastro.Account
import com.pismo.cadastro.OperationType
import com.pismo.cadastro.Transaction
import com.pismo.cadastro.TransactionCommand
import grails.validation.ValidationException
import org.springframework.validation.FieldError

class TransactionService {

    def save(TransactionCommand transactionCommand) {
        Transaction transactionInstance = new Transaction()
        Account accountInstance = Account.get(transactionCommand.account_id.toLong())

        if(!accountInstance){
            accountInstance = new Account(-transactionCommand.amount)
        } else{
            accountInstance.availableWithdrawalLimit -= transactionCommand.amount
            accountInstance.availableCreditLimit -= transactionCommand.amount
        }
        transactionInstance.account = accountInstance
        OperationType operationTypeInstance = OperationType.get(transactionCommand.operation_type_id)
        transactionInstance.operationType = operationTypeInstance
        transactionInstance.dueDate = transactionCommand.due_date
        transactionInstance.amount = -transactionCommand.amount
        transactionInstance.balance = -transactionCommand.amount
        accountInstance.addToTransactions(transactionInstance)
        accountInstance.save(flush: true, failOnError: true)
        transactionInstance
    }

    Map validation(TransactionCommand transactionCommand, Boolean throwExcception = false) {
        def errors = [] as Set<FieldError>
        transactionCommand.validate()
        errors.addAll(transactionCommand.errors.fieldErrors)
        if (throwExcception && errors) {
            throw new ValidationException("Transação ${transactionCommand.account_id ?: "SEM ID DE CONTA"} inválido\n${errors}", transactionCommand.errors)
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
        Boolean invalido = false
        def field
        def message
        OperationType operationTypeInstance = OperationType.get(transactionCommand.operation_type_id)
        if(!operationTypeInstance){
            invalido = true
            field = 'operation_type_id'
            message = "Not found: ${transactionCommand.operation_type_id}"
        } else if(operationTypeInstance == OperationType.PAGAMENTO){
            if(!transactionCommand.paid_date) {
                invalido = true
                field = 'paid_date'
                message = "Missing attribute: ${transactionCommand.paid_date}"
            } else if(transactionCommand.due_date){
                invalido = true
                field = 'due_date'
                message = "Invalid attribute: ${transactionCommand.due_date}"
            }
        } else if(transactionCommand.paid_date) {
            invalido = true
            field = 'paid_date'
            message = "Invalid attribute: ${transactionCommand.paid_date}"
        }
        if(transactionCommand.due_date && transactionCommand.due_date < new Date()){
            invalido = true
            field = 'due_date'
            message = "VENCIDO => ${transactionCommand.due_date}"
        }

        if (invalido) {
            def error = [:]
            error.code = "STATE"
            error.field = field
            error.rejectedValue = message
            responseErrors << error
        }
        [errors: responseErrors]
    }
}
