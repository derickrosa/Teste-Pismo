package com.pismo.cadastro

class PaymentTransaction extends  Transaction {
    Date dateCreated = new Date()
    Date lastUpdated = new Date()

    Date paidDate

    static constraints = {
    }

    static mappedBy = [billings: 'transaction']

    static mapping = {
        paidDate type: 'date'
    }
}
