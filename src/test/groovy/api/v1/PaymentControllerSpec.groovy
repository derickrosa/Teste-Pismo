package api.v1

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class PaymentControllerSpec  extends Specification implements ControllerUnitTest<PaymentController>, DataTest {
    Class<?>[] getDomainClassesToMock(){
        return [Account] as Class[]
    }

    void "test save action"() {
        setup:
        Long account_id = 1L
        BigDecimal availableCreditLimit = 1000.0
        BigDecimal availableWithdrawalLimit = 500.0
        BigDecimal deductedCreditValue = 600
        BigDecimal deductedWithDrawalValue = 500

        Account account = new Account(
                availableCreditLimit: availableCreditLimit - deductedCreditValue,
                availableWithdrawalLimit: availableWithdrawalLimit - deductedWithDrawalValue
        )
        account.metaClass.id = account_id
        account.save(flush: true)

        controller.transactionService = Stub(TransactionService) {
            create(_) >> new Transaction()
            orderedTransactionList(_) >> [new Transaction(), new Transaction(), new Transaction()]
        }
        controller.paymentService = Stub(PaymentService) {
            save(_) >> [deductedCreditValue: deductedCreditValue, deductedWithDrawalValue: deductedWithDrawalValue]
        }
        controller.accountService = Stub(AccountService) {
            update(_,_) >> account
        }

        when:
        request.method = 'POST'
        request.contentType = JSON_CONTENT_TYPE
        request.json = [[amount: 123.45, account_id: account_id], [amount:456.78, account_id: account_id]]

        controller.save()

        then: 'a created response code is used'
        response.status == 201

        and: 'a json response with correct data is returned'
        response.json.find {
            it.account_id == account_id &&
            it.available_credit_limit == account.availableCreditLimit &&
            it.available_withdrawal_limit == account.availableWithdrawalLimit
        }

    }
}
