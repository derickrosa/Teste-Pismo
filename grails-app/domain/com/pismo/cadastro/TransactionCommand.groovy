package com.pismo.cadastro

class TransactionCommand {
    Long account_id
    Long operation_type_id
    BigDecimal amount
    Date due_date
    Date paid_date

    static constraints = {
        account_id          nullable: false
        operation_type_id   nullable: false
        amount              nullable: false
        due_date            nullable: true
        paid_date           nullable: true
    }
}
