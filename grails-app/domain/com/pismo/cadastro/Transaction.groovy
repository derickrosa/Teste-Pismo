package com.pismo.cadastro

import enums.StatusTransaction

class Transaction {
    Date lastUpdated = new Date()

    BigDecimal amount
    BigDecimal balance
    Date eventdate = new Date()
    Date dueDate
    StatusTransaction statusTransaction = StatusTransaction.PURCHASED
    OperationType operationType

    static belongsTo = ['account':Account]
    static hasMany = [billings:TransactionPayment]

    static constraints = {
        amount scale: 2
        balance scale: 2
        dueDate nullable: true
    }

    static mapping = {
        eventdate type: 'date'
        id generator: 'sequence', params: [sequence: 'transaction_seq']
        eventdate index: 'idx_transaction_eventDate'
    }
}
