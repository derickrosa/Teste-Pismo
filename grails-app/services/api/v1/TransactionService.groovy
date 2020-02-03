package api.v1

import grails.gorm.transactions.Transactional

@Transactional
class TransactionService {

    Transaction create(Map params) {
        Account account = Account.get(params.account_id)
        OperationType operationType = OperationType.get(params.operation_type_id)

        Transaction transaction = new Transaction(
                account: account,
                operationType: operationType,
                amount: -params.amount,
                balance: -params.amount
        ).save(failOnError: true)

        if(operationType  == OperationType.PAGAMENTO) return transaction

        account.addAvailableCreditLimit(transaction.amount)
        if (operationType == OperationType.SAQUE)
            account.addAvailableWithdrawalLimit(transaction.amount)

        transaction
    }

    List<Transaction> orderedTransactionList(Account account) {
        List<Transaction> transactionList = Transaction.findAllByAccount(account)

        transactionList.sort{ x, y->
            if(x.operationType.chargeOrder == y.operationType.chargeOrder){
                x.eventDate <=> y.eventDate
            }else{
                x.operationType.chargeOrder <=> y.operationType.chargeOrder
            }
        }
    }
}
