package com.pismo.cadastro

import grails.test.hibernate.HibernateSpec
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest



class TransactionServiceSpec extends HibernateSpec implements ServiceUnitTest<TransactionService>, DataTest {

    def setup() {
        mockDomain(TransactionCommand)
        mockDomain(Transaction)
        mockDomain(OperationType)
        mockDomain(Account)
        setupAccountMock()
        setupOperationType()
    }

    def cleanup() {
    }

    void "Test transaction save"() {
        given:"TransactionCommand criado"
        TransactionCommand transactionCommand = getTransactionCommandData()

        when:"Dado um TransactionCommand retorna uma nova Transaction"
        Transaction result = service.save(transactionCommand)

        then:"A transação deve ser criada e saldo da conta abatido"
        transactionCommand.amount == -result.amount
        transactionCommand.account_id == 100
        transactionCommand.operation_type_id == result.operationType.id
        transactionCommand.due_date == result.dueDate
        result.account.availableCreditLimit == 100
        result.account.availableWithdrawalLimit == 100
    }

    TransactionCommand getTransactionCommandData() {
        def params = ['amount':50, 'account_id':100, 'operation_type_id':1]
        new TransactionCommand(account_id: params.account_id,
                operation_type_id: params.operation_type_id,
                amount: params.amount).save()
    }

    void setupAccountMock(){
        def account = new Account(150)
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
