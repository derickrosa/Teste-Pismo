package com.pismo.cadastro

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TransactionControllerSpec extends Specification implements ControllerUnitTest<TransactionController>, DataTest {
    @Shared TransactionController controller = new TransactionController()

    def setup() {
        mockDomain(Account)
        mockDomain(Transaction)
        mockDomain(TransactionCommand)
        mockDomain(OperationType)
        getAccount()
        setupOperationType()
    }

    def cleanup() {
    }

    void "test POST transactions"() {
        given:
        TransactionService transactionService = new TransactionService()
        controller.transactionService = transactionService
        controller.request.json = [amount:100.0, account_id:200, operation_type_id:1]

        when: "Realiza request ao endpoint save"
        controller.save()
        def response = controller.response

        then: " Cria transação e retorna os limites atualizados das contas"
        Transaction.count() == 1
        response.json.operationType == "À vista"
        response.json.accountAvailableCreditLimit == -100.00
        response.json.accountAvailableWithdrawalLimit == -100.00
        response.status == 201
    }

    def setupOperationType(){
        def operationType=new OperationType(name:'À vista',description:'Transação à vista',chargeOrder: 2)
        operationType.id=1
        operationType.save(failOnError:true, flush:true)
    }

    Account getAccount(){
        Account account1 = new Account(200)
        account1.id = 100
        account1.save(flush:true)

        Account account2 = new Account(300)
        account2.id = 300
        account2.save(flush:true)
    }
}
