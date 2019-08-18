package com.pismo.cadastro

class Account {
    Date dateCreated = new Date()
    Date lastUpdated = new Date()

    BigDecimal availableCreditLimit = 0.0
    BigDecimal availableWithdrawalLimit = 0.0

    static hasMany=[transactions:Transaction]
    static constraints = {
        availableCreditLimit scale: 2
        availableWithdrawalLimit scale: 2
    }

    Account(BigDecimal initialBalance){
        availableCreditLimit = initialBalance
        availableWithdrawalLimit = initialBalance
    }
}
