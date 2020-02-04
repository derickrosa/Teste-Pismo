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
        def json = request.JSON

        Account accountInstance = accountService.create(json)
        if (accountInstance.hasErrors()) {
            render status: HttpStatus.NOT_ACCEPTABLE, text: accountInstance.errors as JSON
            return
        }


        def map = [id: accountInstance.id, accountAvailableCreditLimit: accountInstance.availableCreditLimit,
                   accountAvailableWithdrawalLimit: accountInstance.availableWithdrawalLimit]

        render contentType: 'application/json', status: HttpStatus.CREATED, text: map as JSON
    }

}
