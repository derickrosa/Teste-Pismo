package com.pismo.cadastro

import grails.converters.JSON
import grails.rest.RestfulController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

class TransactionController extends RestfulController {

    def transactionService
    static responseFormats = ['json']

    TransactionController() {
        super(Transaction)
    }

    @RequestMapping(method = RequestMethod.POST)
    def save(){
        try{
            def json = request.JSON
            def transactionCommand = new TransactionCommand()
            bindData(transactionCommand, json)
            def result = transactionService.validation(transactionCommand)
            if (result?.errors) {
                println "Erros!!11"
                render status: HttpStatus.NOT_ACCEPTABLE, text: result as JSON
                return
            }
            println "Sucesso"
            Transaction instance = transactionService.save(transactionCommand)
            def map = [id: instance.id, eventDate: instance.eventdate, operationType: instance.operationType.name,
                       amount:instance.amount, accountAvailableCreditLimit: instance.account.availableCreditLimit,
                       accountAvailableWithdrawalLimit: instance.account.availableWithdrawalLimit]

            render contentType: 'application/json', status: HttpStatus.CREATED, text: map as JSON
        } catch (Exception e) {
            println "Erro?"
            log.error("Error:", e)
            def map = [errors: []]
            map.errors << [code: "GENERIC_ERROR"]
            render status: HttpStatus.INTERNAL_SERVER_ERROR, text: map as JSON
        }
    }

}
