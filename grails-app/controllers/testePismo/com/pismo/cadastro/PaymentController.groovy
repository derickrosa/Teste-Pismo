package testePismo.com.pismo.cadastro

import com.pismo.cadastro.OperationType
import com.pismo.cadastro.PaymentTransaction
import com.pismo.cadastro.TransactionCommand
import grails.converters.JSON
import org.springframework.http.HttpStatus

class PaymentController {

    def paymentService
    def transactionService
    static allowedMethods = [save: "POST"]

    def save() {
        try{
            def json = request.JSON
            def paymentCommandList = []
            json.each { payment ->
                TransactionCommand paymentCommand = new TransactionCommand(payment)
                paymentCommand.operation_type_id = OperationType.PAGAMENTO.id
                paymentCommand.paid_date = new Date()
                def result = transactionService.validation(paymentCommand)
                if (result?.errors) {
                    render status: HttpStatus.NOT_ACCEPTABLE, text: result as JSON
                    return
                }
                paymentCommandList << paymentCommand

            }
            List<PaymentTransaction> instanceList = paymentService.save(paymentCommandList)
            def map = instanceList.collect { it.id }
            render status: HttpStatus.CREATED, text: map as JSON
        } catch (Exception e) {
                log.error("Error:", e)
                def map = [errors: []]
                map.errors << [code: "GENERIC_ERROR"]
                render status: HttpStatus.INTERNAL_SERVER_ERROR, text: map as JSON
        }
    }
}
