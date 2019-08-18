package testePismo.com.pismo.cadastro

import com.pismo.cadastro.Account
import com.pismo.cadastro.AccountCommand
import grails.converters.JSON
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

class AccountController {

    def accountService

    def limits() {
        HashMap jsonMap = new HashMap()
        List listOfAccounts = Account.list()
        jsonMap.accounts = listOfAccounts.collect { Account account ->
            return [id: account.id, availableCreditLimit: account.availableCreditLimit, availableWithdrawalLimit: account.availableWithdrawalLimit]
        }
        render jsonMap as JSON
    }

    @RequestMapping(method = RequestMethod.PATCH)
    def update(Long id){
        println "Chegou aqui: ${id}"
        def json = request.JSON
        def accountCommand = new AccountCommand()
        accountCommand.available_credit_limit = json.available_credit_limit.amount
        accountCommand.available_withdrawal_limit = json.available_withdrawal_limit.amount
        def result = accountService.validation(accountCommand)
        if (result?.errors) {
            render status: HttpStatus.NOT_ACCEPTABLE, text: result as JSON
            return
        }
        Account account = Account.get(id)
        Account accountInstance = accountService.update(accountCommand, account)
        def map = [id: accountInstance.id, availableCreditLimit: accountInstance.availableCreditLimit, availableWithdrawalLimit: accountInstance.availableWithdrawalLimit]
        render status: HttpStatus.OK, text: map as JSON
    }
}
