package com.pismo.cadastro

import enums.StatusTransaction
import grails.test.hibernate.HibernateSpec
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest


class PaymentServiceSpec extends HibernateSpec implements ServiceUnitTest<PaymentService>, DataTest {

    def setup() {
        mockDomain(Transaction)
        mockDomain(TransactionCommand)
        mockDomain(TransactionPayment)
        mockDomain(OperationType)
        mockDomain(Account)
        mockDomain(PaymentTransaction)
        setupAccountMock()
        setupOperationType()
        setupTransactionsMock()
    }

    def cleanup() {
    }

    void "Test Payment save"() {
        given:"Lista de comandos de transação de pagamento"
        TransactionCommand transactionCommand = new TransactionCommand(id: 1000L, account_id: 100, operation_type_id: 4, amount: new BigDecimal(100), paid_date: new Date())

        when:"Dado uma comandos de transação de pagamento retorna lista de pagamentos"
        PaymentTransaction result = service.save(transactionCommand)

        then:"O account deve conter os limites atualizados"
        PaymentTransaction.count() == 1
        result != null
        result.statusTransaction == StatusTransaction.PURCHASED
    }

    void "Test charge transaction"() {
        given: "Pagamento e lista de transações a serem pagas"
        PaymentTransaction paymentTransaction = new PaymentTransaction(account: 100, operationType: OperationType.PAGAMENTO, amount: new BigDecimal(100), paidDate: new Date())
        List<Long> transactions = setupTransactionsMock().collect {it.id}

        when: "Realiza o pagamento das transações"
        service.charge(transactions, paymentTransaction)

        then: "As transações devem ter seus status alterados para pago"
        paymentTransaction.statusTransaction == StatusTransaction.PAID
    }


    List<Transaction> setupTransactionsMock() {
        Account account = Account.get(100)
        List<Transaction> transactions = []
        transactions << new Transaction(account: account, operationType: OperationType.SAQUE, amount: 50, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "20/08/2019"))
        transactions << new Transaction(account: account, operationType: OperationType.PARCELADA, amount: 100, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "15/08/2019"))
        transactions << new Transaction(account: account, operationType: OperationType.PARCELADA, amount: 150, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "10/08/2019"))
        transactions << new Transaction(account: account, operationType: OperationType.AVISTA, amount: 200, balance: 200, eventdate: Date.parse("dd/MM/yyyy", "05/08/2019"))
        transactions
    }

    void setupAccountMock(){
        def account = new Account(200)
        account.id = 100
        account.save(flush:true)
    }

    def setupOperationType() {
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
}
