package api.v1

import grails.gorm.transactions.Transactional

@Transactional
class TransactionService {

    def accountService

    Transaction create(Map params) {
        Account account = Account.get(params.account_id)
        OperationType operationType = getOperationType(params.operation_type_id)

        Transaction transaction = new Transaction(
                account: account,
                operationType: operationType,
                amount: params.amount,
                balance: params.amount
        ).save(failOnError: true, flush: true)

        if(operationType  == OperationType.PAGAMENTO) return transaction

        accountService.addAvailableCreditLimit(account, transaction.amount)
        if (operationType == OperationType.SAQUE)
            accountService.addAvailableWithdrawalLimit(account, transaction.amount)

        transaction
    }

    OperationType getOperationType(Long id){
        if(!id) return OperationType.PAGAMENTO
        OperationType.get(id)
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
