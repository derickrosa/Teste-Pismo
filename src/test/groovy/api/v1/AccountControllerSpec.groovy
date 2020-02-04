package api.v1

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController> {

    def setup() {
        Account.metaClass.'static'.list = {
            [new Account(availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00),
             new Account(availableCreditLimit: 500.00, availableWithdrawalLimit: 250.00),
             new Account(availableCreditLimit: 250.00, availableWithdrawalLimit: 125.00)]
        }
    }

    void "test limits action"() {
        when:"The limits action is invoked"
        controller.limits()

        then:"A list of accounts info is returned in JSON format"
        response.json.find { it.availableCreditLimit == 1000.00 && it.availableWithdrawalLimit == 500.00 }
        response.json.find { it.availableCreditLimit == 500.00 && it.availableWithdrawalLimit == 250.00 }
        response.json.find { it.availableCreditLimit == 250.00 && it.availableWithdrawalLimit == 125.00 }
        response.status == 200
    }

    void "test save action"() {
        given:
        BigDecimal availableCreditLimit = 1000.00
        BigDecimal availableWithdrawalLimit = 50.00

        controller.accountService = Stub(AccountService) {
            create(_, _) >> new Account(availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00)
        }

        when:
        request.method = 'POST'
        request.json = [availableCreditLimit: 1000.00, availableWithdrawalLimit: 500.00]

        controller.save()

        then: 'a found response code is used'
        response.status == 201
    }
}
