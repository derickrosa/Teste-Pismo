package api.v1

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class AccountServiceSpec  extends Specification implements ServiceUnitTest<AccountService>, DataTest {
    @Shared Account account

    void setupSpec() {
        mockDomain(Account)
    }

    def setup() {
        account =  new Account(availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00).save()
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
}
