package com.pismo.cadastro

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Shared
import spock.lang.Specification

class PaymentControllerSpec extends Specification implements ControllerUnitTest<PaymentController>, DataTest {
    @Shared PaymentController controller = new PaymentController()

    def setup() {
        mockDomain(Account)
        mockDomain(Transaction)
        mockDomain(TransactionCommand)
        mockDomain(OperationType)
        getAccount()
        setupOperationType()
        createTransactions()
    }

    def cleanup() {
    }

    void "test POST payments"() {
        given:
        TransactionService transactionService = new TransactionService()
        PaymentService paymentService = new PaymentService()
        controller.transactionService = transactionService
        controller.paymentService = paymentService
        controller.request.json = [[amount:150, account_id:100], [amount:150, account_id:100]]

        when: "Realiza request ao endpoint save"
        controller.save()
        def response = controller.response

        then: " Cria pagamentos e retorna lista com processados"
        PaymentTransaction.count() == 2
        response.status == 201
    }

    def setupOperationType(){
        if (!OperationType.get(1)) {
            def operationType=new OperationType(name:'À vista',description:'Transação à vista',chargeOrder: 2)
            operationType.id=1
            operationType.save(failOnError:true, flush:true)
        }
        if (!OperationType.get(2)) {
            def operationType=new OperationType(name:'Parcelada',description:'Transação parcelada',chargeOrder: 1)
            operationType.id=2
            operationType.save(failOnError:true, flush:true)
        }
        if (!OperationType.get(3)) {
            def operationType=new OperationType(name:'Saque',description:'Transação de saque',chargeOrder: 0)
            operationType.id=3
            operationType.save(failOnError:true, flush:true)
        }
        if (!OperationType.get(4)) {
            def operationType=new OperationType(name:'Pagamento',description:'Transação de Pagamento',chargeOrder: 0)
            operationType.id=4
            operationType.save(failOnError:true, flush:true)
        }
    }

    Account getAccount(){
        Account account1 = new Account(200)
        account1.id = 100
        account1.save(flush:true)

        Account account2 = new Account(300)
        account2.id = 300
        account2.save(flush:true)
    }

    void createTransactions(Long id = 100) {
        Account account = Account.get(id)
        Account.saveAll(
                new Transaction(account: account, operationType: OperationType.SAQUE, amount: 50, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "20/08/2019")),
                new Transaction(account: account, operationType: OperationType.PARCELADA, amount: 100, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "15/08/2019")),
                new Transaction(account: account, operationType: OperationType.PARCELADA, amount: 150, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "10/08/2019")),
                new Transaction(account: account, operationType: OperationType.AVISTA, amount: 200, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "05/08/2019"))
        )
    }
}
