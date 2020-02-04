package api.v1

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class PaymentServiceSpec  extends Specification implements ServiceUnitTest<PaymentService>, DataTest {
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

    void "test process payment"() {
        given:
        List<Transaction> orderedTransactionsList = []
        orderedTransactionsList << new Transaction(
                amount: 50.0,
                balance: 50.0,
                account: account,
                operationType: OperationType.SAQUE,
                eventDate: Date.parse("dd/MM/yyyy", "05/04/2017")
        ).save(failOnError: true, flush: true)

        orderedTransactionsList << new Transaction(
                amount: 23.50,
                balance: 23.50,
                account: account,
                operationType: OperationType.AVISTA,
                eventDate: Date.parse("dd/MM/yyyy", "10/04/2017")
        ).save(failOnError: true, flush: true)

        orderedTransactionsList << new Transaction(
                amount: 18.7,
                balance: 18.7,
                account: account,
                operationType: OperationType.AVISTA,
                eventDate: Date.parse("dd/MM/yyyy", "30/04/2017")
        ).save(failOnError: true, flush: true)


        Transaction payment = new Transaction(
                amount: 70.0,
                balance: 70.0,
                account: account,
                operationType: OperationType.PAGAMENTO,
                eventDate: Date.parse("dd/MM/yyyy", "30/04/2017")
        ).save(failOnError: true, flush: true)


        when:
        def processing_result = service.processPayment(payment, orderedTransactionsList)

        then: ' should return a map object containing deducted values accordingly'
        processing_result == [deductedCreditValue: 70, deductedWithDrawalValue: 50]

        and: 'payment values should be deducted from the balance values accordingly'
        orderedTransactionsList[0].balance == 0
        orderedTransactionsList[1].balance == -3.5
        orderedTransactionsList[2].balance == -18.7
        payment.balance == 0
    }


}
