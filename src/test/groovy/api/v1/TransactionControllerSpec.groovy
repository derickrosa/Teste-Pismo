package api.v1

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification


class TransactionControllerSpec extends Specification implements ControllerUnitTest<TransactionController>{

    void "test save action"() {
        setup:
        BigDecimal amount = 123.45
        Long operatioType_id = 1L
        Long account_id = 1L
        Long transaction_id = 1L

        Account account = new Account()
        account.metaClass.id = account_id

        OperationType operationType = new OperationType()
        account.metaClass.id = operatioType_id

        Transaction transaction = new Transaction(
                amount: amount,
                balance: amount,
                account: account,
                operationType: operationType
        )
        transaction.metaClass.id = transaction_id

        controller.transactionService = Stub(TransactionService) {
             create(_) >> transaction
        }

        when:
        request.method = 'POST'
        request.contentType = JSON_CONTENT_TYPE
        request.json = [amount:amount, account_id: account_id, operation_type_id: operatioType_id]

        controller.save()

        then: 'a created response code is used'
        response.status == 201

        and: 'a json response with correct data is returned'
        response.json.account_id == account_id
        response.json.transaction_id == transaction_id
        response.json.amount == amount
        response.json.balance == amount

    }
}
