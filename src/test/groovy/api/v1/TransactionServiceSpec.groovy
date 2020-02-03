package api.v1

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TransactionServiceSpec  extends Specification implements ServiceUnitTest<TransactionService>, DataTest {
    @Shared Account account
    @Shared OperationType operationType

    void setupSpec() {
        mockDomains(Account, OperationType, Transaction)
    }

    def setup() {
        account =  new Account(availableCreditLimit: 1000.0, availableWithdrawalLimit: 500.0).save()

        def operationTypeDebit = new OperationType(description: 'COMPRA A VISTA', chargeOrder: 2)
        operationTypeDebit.id = 1
        operationTypeDebit.save()

        def operationTypeParcel = new OperationType(description: 'COMPRA PARCELADA', chargeOrder: 1)
        operationTypeParcel.id = 2
        operationTypeParcel.save()

        def operationTypeWithDrawal = new OperationType(description: 'SAQUE', chargeOrder: 0)
        operationTypeWithDrawal.id = 3
        operationTypeWithDrawal.save()

        def operationTypePayment = new OperationType(description: 'PAGAMENTO', chargeOrder: 0)
        operationTypePayment.id = 4
        operationTypePayment.save()
    }

    void "test creating debit transaction"() {
        given:
        Map params = ["account_id": account.id, "operation_type_id": 1, "amount": 100.0]

        when:
        def instance = service.create(params)

        then:
        Transaction.count() == 1

        and: 'should return a Transaction'
        instance instanceof Transaction

        and: 'should write down only the account available credit limit'
        account.availableCreditLimit == 900.0
        account.availableWithdrawalLimit == 500.0
    }

    void "test creating withdrawal transaction"() {
        given:
        Map params = ["account_id":account.id, "operation_type_id": 3, "amount": 100.0]

        when:
        def instance = service.create(params)

        then:
        Transaction.count() == 1

        and: 'should return a Transaction'
        instance instanceof Transaction

        and: 'should write down the account available credit and withdrawal limits'
        account.availableCreditLimit == 900.0
        account.availableWithdrawalLimit == 400.0
    }

    void "test return transaction list ordered by chargeOrder and eventDate"() {
        given:
        new Transaction(
                amount: -23.50,
                balance: -23.50,
                account: account,
                operationType: OperationType.AVISTA,
                eventDate: Date.parse("dd/MM/yyyy", "10/04/2017")
        ).save(failOnError: true)

        new Transaction(
                amount: -18.7,
                balance: -18.7,
                account: account,
                operationType: OperationType.PARCELADA,
                eventDate: Date.parse("dd/MM/yyyy", "30/04/2017")
        ).save(failOnError: true)

        new Transaction(
                amount: -70.0,
                balance: -70.0,
                account: account,
                operationType: OperationType.PAGAMENTO,
                eventDate: Date.parse("dd/MM/yyyy", "30/04/2017")
        ).save(failOnError: true)

        new Transaction(
                amount: -50.0,
                balance: -50.0,
                account: account,
                operationType: OperationType.SAQUE,
                eventDate: Date.parse("dd/MM/yyyy", "05/04/2017")
        ).save(failOnError: true)

        when:
        def list = service.orderedTransactionList(account)

        then: 'should return an ordered transaction list'
        list[0].operationType == OperationType.SAQUE
        list[1].operationType == OperationType.PAGAMENTO
        list[2].operationType == OperationType.PARCELADA
        list[3].operationType == OperationType.AVISTA
    }
}
