package api.v1

import grails.converters.JSON
import org.springframework.http.HttpStatus

class AccountController {

    def accountService

    def limits() {
        HashMap jsonMap = new HashMap()
        jsonMap.accounts = Account.list().collect { Account account ->
            return [id: account.id, availableCreditLimit: account.availableCreditLimit, availableWithdrawalLimit: account.availableWithdrawalLimit]
        }
        render contentType: 'application/json', jsonMap.accounts as JSON, status: HttpStatus.OK
    }

    def save() {
        def params = request.JSON

        Account accountInstance = accountService.create(params)
        if (accountInstance.hasErrors()) {
            render status: HttpStatus.NOT_ACCEPTABLE, text: accountInstance.errors as JSON
            return
        }


        def map = [id: accountInstance.id, availableCreditLimit: accountInstance.availableCreditLimit,
                   availableWithdrawalLimit: accountInstance.availableWithdrawalLimit]

        render contentType: 'application/json', status: HttpStatus.CREATED, text: map as JSON
    }

    def update(Long id) {
        def params = request.JSON
        Account account = Account.get(id)

        Account accountInstance = accountService.update(account, params)
        if (accountInstance.hasErrors()) {
            render status: HttpStatus.NOT_ACCEPTABLE, text: accountInstance.errors as JSON
            return
        }

        def map = [id: accountInstance.id, availableCreditLimit: accountInstance.availableCreditLimit, availableWithdrawalLimit: accountInstance.availableWithdrawalLimit]
        render contentType: 'application/json', status: HttpStatus.OK, text: map as JSON
    }
}
