package testePismo.com.pismo.cadastro

import com.pismo.cadastro.Transaction
import com.pismo.cadastro.TransactionCommand
import grails.converters.JSON
import org.springframework.http.HttpStatus

class TransactionController {

    def transactionService
    static allowedMethods = [save: "POST"]

    def save(){
        try{
            def json = request.JSON
            def transactionCommand = new TransactionCommand()
            bindData(transactionCommand, json)
            def result = transactionService.validation(transactionCommand)
            if (result?.errors) {
                render status: HttpStatus.NOT_ACCEPTABLE, text: result as JSON
                return
            }
            Transaction instance = transactionService.save(transactionCommand)
            def map = [id: instance.id, eventDate: instance.eventdate, operationType: instance.operationType.name,
                       amount:instance.amount, accountAvailableCreditLimit: instance.account.availableCreditLimit,
                       accountAvailableWithdrawalLimit: instance.account.availableWithdrawalLimit]
            render status: HttpStatus.CREATED, text: map as JSON
        } catch (Exception e) {
            log.error("Error:", e)
            def map = [errors: []]
            map.errors << [code: "GENERIC_ERROR"]
            render status: HttpStatus.INTERNAL_SERVER_ERROR, text: map as JSON
        }
    }

}
