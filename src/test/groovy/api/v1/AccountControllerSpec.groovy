package api.v1

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
        setup:
        BigDecimal availableCreditLimit = 1000.0
        BigDecimal availableWithdrawalLimit = 500.0
        Long id = 1L
        Account account = new Account(availableCreditLimit: availableCreditLimit, availableWithdrawalLimit: availableWithdrawalLimit)
        account.metaClass.id = id

        controller.accountService = Stub(AccountService) {
            create(_) >> account
        }

        when:
        request.method = 'POST'
        request.contentType = JSON_CONTENT_TYPE
        request.json = [availableCreditLimit: availableCreditLimit, availableWithdrawalLimit: availableWithdrawalLimit]

        controller.save()

        then: 'a created response code is used'
        response.status == 201

        and: 'a json response with correct data is returned'
        response.json.id == id
        response.json.availableCreditLimit == availableCreditLimit
        response.json.availableWithdrawalLimit == availableWithdrawalLimit
    }
}
