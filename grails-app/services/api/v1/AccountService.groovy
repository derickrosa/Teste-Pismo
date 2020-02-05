package api.v1

import grails.gorm.transactions.Transactional
import jxl.write.Boolean

@Transactional
class AccountService {

    Account create(Map params) {
        Account account = new Account(
            availableCreditLimit: params.available_credit_limit.amount,
            availableWithdrawalLimit: params.available_withdrawal_limit.amount
        )

        account.save(flush: true)
    }

    Account update(Account account, Map params) {
        addAvailableCreditLimit(account, params.available_credit_limit?.amount)
        addAvailableWithdrawalLimit(account, params.available_withdrawal_limit?.amount)

        account.save(flush: true)
    }

    void addAvailableCreditLimit(Account account, BigDecimal amount){
        account.availableCreditLimit += amount
        account.save(flush: true)
    }

    void addAvailableWithdrawalLimit(Account account, BigDecimal amount){
        account.availableWithdrawalLimit += amount
        account.save(flush: true)
    }
}
