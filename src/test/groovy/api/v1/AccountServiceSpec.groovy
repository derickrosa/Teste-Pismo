package api.v1

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class AccountServiceSpec  extends Specification implements ServiceUnitTest<AccountService>, DataTest {
    @Shared Account account

    void setupSpec() {
        mockDomains(Account, Transaction)
    }

    def setup() {
        account =  new Account(availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00).save(flush: true)
        def operationTypeDebit = new OperationType(description: 'COMPRA A VISTA', chargeOrder: 2)
        operationTypeDebit.id = 1
        operationTypeDebit.save()

        def operationTypeWithDrawal = new OperationType(description: 'SAQUE', chargeOrder: 0)
        operationTypeWithDrawal.id = 3
        operationTypeWithDrawal.save()

        def operationTypePayment = new OperationType(description: 'PAGAMENTO', chargeOrder: 0)
        operationTypePayment.id = 4
        operationTypePayment.save()
    }

    void "test creating account"() {
        given:
        Map params = ["available_credit_limit":["amount":1000.0], "available_withdrawal_limit":["amount":500.00]]

        when:
        def instance = service.create(params)

        then:
        Account.count() == 2

        and:
        instance instanceof Account
    }

    void "test updating account limits"() {
        given:
        Map params = ["available_credit_limit":["amount":123.45], "available_withdrawal_limit":["amount":123.45]]

        when:
        def instance = service.update(account, params)

        then:
        account.availableCreditLimit == 1123.45
        account.availableWithdrawalLimit == 623.45

        and:
        instance instanceof Account
    }

    void "test debit transaction from account limits"() {
        given:
        Transaction transactionInstance = new Transaction(
                amount: 100.0,
                balance: 100.0,
                account: account,
                operationType: OperationType.AVISTA,
        ).save(flush:true, failOnError: true)

        when:
        service.deductTransaction(transactionInstance)

        then: 'should debit only availableCreditLimit'
        account.availableCreditLimit == 900.0
        account.availableWithdrawalLimit == 500.0
    }

    void "test debit withdrawal transaction from account limits"() {
        given:
        Transaction transactionInstance = new Transaction(
                amount: 100.0,
                balance: 100.0,
                account: account,
                operationType: OperationType.SAQUE,
        ).save(flush:true, failOnError: true)

        when:
        service.deductTransaction(transactionInstance)

        then:'should debit both availableCreditLimit and availableWithdrawalLimit'
        account.availableCreditLimit == 900.0
        account.availableWithdrawalLimit == 400.0
    }

    void "test payment transaction from account limits"() {
        given:
        Transaction transactionInstance = new Transaction(
                amount: 100.0,
                balance: 100.0,
                account: account,
                operationType: OperationType.PAGAMENTO,
        ).save(flush:true, failOnError: true)

        when:
        service.deductTransaction(transactionInstance)

        then: 'nshould debit nothing from account limits'
        account.availableCreditLimit == 1000.0
        account.availableWithdrawalLimit == 500.0
    }
}
