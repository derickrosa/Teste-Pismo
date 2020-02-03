package api.v1

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class AccountSpec extends Specification implements DomainUnitTest<Account> {
    @Shared int id

    void "test basic persistence mocking"() {
        setup:
        new Account(availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00).save()
        new Account(availableCreditLimit: 2000.00, availableWithdrawalLimit: 1000.00).save()

        expect:
        Account.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.availableCreditLimit = 1000.00

        then:
        domain.availableCreditLimit == 1000.00
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.availableCreditLimit == 0.0
        domain.availableWithdrawalLimit == 0.0
        System.identityHashCode(domain) != id
    }

    void 'test availableCreditLimit cannot be null'() {
        when:
        domain.availableCreditLimit = null

        then:
        !domain.validate(['availableCreditLimit'])
        domain.errors['availableCreditLimit'].code == 'nullable'
    }

    void 'test availableWithdrawalLimit cannot be null'() {
        when:
        domain.availableWithdrawalLimit = null

        then:
        !domain.validate(['availableWithdrawalLimit'])
        domain.errors['availableWithdrawalLimit'].code == 'nullable'
    }

    void 'test availableCreditLimit validation'() {
        when:
        domain.availableCreditLimit = value

        then:
        expected == domain.validate(['availableCreditLimit'])
        domain.errors['availableCreditLimit']?.code == expectedErrorCode

        where:
        value                   | expected | expectedErrorCode
        null                    | false    | 'nullable'
        0                       | true     | null
        0.5                     | true     | null
        1000                    | true     | null
        -90.5                   | false    | 'balance.insufficient'
    }

    void 'test availableWithdrawalLimit validation'() {
        when:
        domain.availableWithdrawalLimit = value

        then:
        expected == domain.validate(['availableWithdrawalLimit'])
        domain.errors['availableWithdrawalLimit']?.code == expectedErrorCode

        where:
        value                   | expected | expectedErrorCode
        null                    | false    | 'nullable'
        0                       | true     | null
        0.5                     | true     | null
        1000                    | true     | null
        -90.5                   | false    | 'balance.insufficient'
    }


    void "test addAvailableCreditLimit()"() {
        setup:
        def account = new Account(availableCreditLimit: 800.00).save()

        when:
        account.addAvailableCreditLimit(200.00)

        then:
        account.availableCreditLimit == 1000.00
    }

    void "test addAvailableWithdrawalLimit()"() {
        setup:
        def account = new Account(availableWithdrawalLimit: 500.00).save()

        when:
        account.addAvailableWithdrawalLimit(200.00)

        then:
        account.availableWithdrawalLimit == 700.00
    }
}
