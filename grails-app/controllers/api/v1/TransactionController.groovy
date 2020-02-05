package api.v1

import grails.converters.JSON
import org.springframework.http.HttpStatus

class TransactionController {

    def transactionService
    def accountService

    def save() {
        def params = request.JSON

        Transaction transactionInstance = transactionService.create(params)
        if (transactionInstance.hasErrors()) {
            render status: HttpStatus.NOT_ACCEPTABLE, text: transactionInstance.errors as JSON
            return
        }

        accountService.deductTransaction(transactionInstance)
        def map = [transaction_id: transactionInstance.id, account_id: transactionInstance.account.id,
                   amount:transactionInstance.amount, balance:transactionInstance.balance]

        render contentType: 'application/json', status: HttpStatus.CREATED, text: map as JSON
    }
}
