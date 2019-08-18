package com.pismo.cadastro

class TransactionPayment {
    Transaction transaction
    PaymentTransaction payment
    BigDecimal paymentAmount

    static constraints = {
        paymentAmount scale: 2
        payment nullable: true
        transaction nullable: true
    }
}
