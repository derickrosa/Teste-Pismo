package api.v1

import grails.converters.JSON
import org.springframework.http.HttpStatus

class PaymentController {

    TransactionService transactionService
    PaymentService paymentService
    AccountService accountService

    def save() {
        def params = request.JSON
        List<Map> map = []

        params.each { payment ->
            Account account = Account.get(payment.account_id)
            List<Transaction> transactionList = transactionService.orderedTransactionList(account)
            if (transactionList) {
                Transaction paymentTransaction = transactionService.create(payment)

                if (paymentTransaction.hasErrors()) {
                    render status: HttpStatus.NOT_ACCEPTABLE, text: paymentTransaction.errors as JSON
                    return
                }

                Map deductedValues = paymentService.processPayment(paymentTransaction, transactionList)
                accountService.update(account, [available_credit_limit:[amount: deductedValues.deductedCreditValue],
                                                available_withdrawal_limit:[amount: deductedValues.deductedWithDrawalValue]])

                map << [account_id: account.id, available_credit_limit: account.availableCreditLimit, available_withdrawal_limit: account.availableWithdrawalLimit]
            }
        }

        render contentType: 'application/json', status: HttpStatus.CREATED, text: map as JSON
    }
}
