package com.pismo.cadastro

import grails.test.hibernate.HibernateSpec
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

class AccountServiceSpec extends HibernateSpec implements ServiceUnitTest<AccountService>, DataTest {
    FixtureService fixtureService
    def setup() {
        mockDomain(AccountCommand)
        mockDomain(Account)
    }

    def cleanup() {
    }

    void "Test Acoount save"() {
        given:"AccountCommand e account a ser atualizada"
        Account account = getAccount()
        AccountCommand accountCommand = getAccountCommand()

        when:"Dando um AccountCommand retorna Account"
        Account result = service.update(accountCommand, account)

        then:"O account deve conter os limites atualizados"
        result.availableCreditLimit == 100
        result.availableWithdrawalLimit == 0
    }

    TransactionCommand getTransactionCommandData() {
        def params = ['amount':50, 'account_id':100, 'operation_type_id':1]
        new TransactionCommand(account_id: params.account_id,
                operation_type_id: params.operation_type_id,
                amount: params.amount).save()
    }

    Account getAccount(){
        Account account = new Account(200)
        account.id = 100
        account.save(flush:true)
    }

    AccountCommand getAccountCommand(){
        AccountCommand accountCommand = new AccountCommand()
        accountCommand.id = 100
        accountCommand.available_credit_limit = 100
        accountCommand.available_withdrawal_limit = 200
        accountCommand.save(flush:true)
    }
}
