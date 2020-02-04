package api.v1

import grails.gorm.transactions.Transactional

@Transactional
class AccountService {

    Account create(Map params) {
        Account account = new Account(
            availableCreditLimit: params.available_credit_limit.amount,
            availableWithdrawalLimit: params.available_withdrawal_limit.amount
        )

        account.save()
    }

    Account update(Account account, Map params) {
        account.addAvailableCreditLimit(params.available_credit_limit?.amount)
        account.addAvailableWithdrawalLimit(params.available_withdrawal_limit?.amount)

        account.save()
    }
}
