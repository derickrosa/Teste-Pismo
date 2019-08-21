package com.pismo.cadastro

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Shared
import spock.lang.*



class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController>, DataTest {

    @Shared AccountController controller = new AccountController()

    def setup() {
        mockDomain(Account)
        mockDomain(AccountCommand)
        getAccount()
    }

    def cleanup() {
    }

    void "test GET accounts"() {
        when: "Realiza request ao endpoint limits()"
        // Set request JSON body
        controller.request
        controller.limits()
        def response = controller.response

        then: "Retorna as lista de contas"
        response.json.accounts.id == [100,300]
        response.status == 200
    }

    void "test PATCH accounts"() {
        given:
        AccountService accountService = new AccountService()
        controller.accountService = accountService
        controller.request.json = [available_withdrawal_limit:[amount:50], available_credit_limit:[amount:100]]

        when: "Realiza request ao endpoint update"
        controller.update(100)
        def response = controller.response

        then: "Retorna os limites atualizados das contas"
        response.json.availableCreditLimit == 100.00
        response.json.availableWithdrawalLimit == 150.00
        response.status == 200
    }

    Account getAccount(){
        Account account1 = new Account(200)
        account1.id = 100
        account1.save(flush:true)

        Account account2 = new Account(300)
        account2.id = 300
        account2.save(flush:true)
    }
}
