package api.v1

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TransactionSpec extends Specification implements DomainUnitTest<Transaction> {
    @Shared int id

    void "test basic persistence mocking"() {
        setup:
        def operationType = new OperationType(description: 'SAQUE', chargeOrder: 0).save()
        def account =  new Account(availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00).save()

        new Transaction(
                amount: -50.0,
                balance: -50.0,
                account: account,
                operationType: operationType
        ).save(failOnError: true)

        new Transaction(
                amount: -23.50,
                balance: -23.50,
                account: account,
                operationType: operationType
        ).save(failOnError: true)


        expect:
        Transaction.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.amount = -50.00

        then:
        domain.amount == -50.00
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.amount == null
        domain.eventDate.toString() == new Date().toString()
        System.identityHashCode(domain) != id
    }

    void 'test amount cannot be negative'() {
        when: 'operationType is PAYMENT'
        domain.amount = value
        domain.operationType = OperationType.PAGAMENTO


        then:
        expected == domain.validate(['amount'])
        domain.errors['amount']?.code == expectedErrorCode

        where:
        value                   | expected | expectedErrorCode
        null                    | false    | 'nullable'
        0                       | true     | null
        0.5                     | true     | null
        1000                    | true     | null
        -100.0                  | false    | 'amount.invalid'
    }

    void 'test balance cannot be negative'() {
        when: 'operationType is PAYMENT'
        domain.balance = value
        domain.operationType = OperationType.PAGAMENTO


        then:
        expected == domain.validate(['balance'])
        domain.errors['balance']?.code == expectedErrorCode

        where:
        value                   | expected | expectedErrorCode
        null                    | false    | 'nullable'
        0                       | true     | null
        0.5                     | true     | null
        1000                    | true     | null
        -100.0                  | false    | 'balance.invalid'
    }

    void 'test dueDate can be null'() {
        when:
        domain.dueDate = null

        then:
        domain.validate(['dueDate'])
    }

    void 'test amount cannot be null'() {
        when:
        domain.amount = null

        then:
        !domain.validate(['amount'])
        domain.errors['amount'].code == 'nullable'
    }

    void 'test balance cannot be null'() {
        when:
        domain.balance = null

        then:
        !domain.validate(['balance'])
        domain.errors['balance'].code == 'nullable'
    }

    void 'test eventdate cannot be null'() {
        when:
        domain.eventDate = null

        then:
        !domain.validate(['eventDate'])
        domain.errors['eventDate'].code == 'nullable'
    }

    void 'test account cannot be null'() {
        when:
        domain.account = null

        then:
        !domain.validate(['account'])
        domain.errors['account'].code == 'nullable'
    }

    void 'test operationType cannot be null'() {
        when:
        domain.operationType = null

        then:
        !domain.validate(['operationType'])
        domain.errors['operationType'].code == 'nullable'
    }
}
