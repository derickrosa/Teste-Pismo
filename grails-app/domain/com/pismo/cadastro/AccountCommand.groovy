package com.pismo.cadastro

class AccountCommand {

    BigDecimal available_credit_limit
    BigDecimal available_withdrawal_limit

    static constraints = {
        available_credit_limit scale: 2
        available_withdrawal_limit scale: 2
    }


}
