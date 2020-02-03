package api.v1

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    Map processPayment(Transaction payment, List<Transaction> transactionsList) {
        BigDecimal deductedCreditValue = 0
        BigDecimal deductedWithDrawalValue = 0
        transactionsList.each {transaction ->
            BigDecimal deductedValue = 0

            if (payment.balance + transaction.balance >= 0){
                deductedValue += -transaction.balance
                payment.balance += transaction.balance
                transaction.balance = 0
            } else {
                deductedValue += payment.balance
                transaction.balance += payment.balance
                payment.balance = 0
            }
            transaction.save()

            if (transaction.operationType == OperationType.SAQUE) {
                deductedWithDrawalValue += deductedValue
            }
            deductedCreditValue += deductedValue
        }

        payment.save()

        [deductedCreditValue: deductedCreditValue, deductedWithDrawalValue: deductedWithDrawalValue]
    }
}
