package api.v1


@grails.persistence.Entity
class Transaction {

    BigDecimal amount
    BigDecimal balance
    Date eventDate = new Date()
    Date dueDate

    OperationType operationType

    static belongsTo = [account: Account]


    static constraints = {
        dueDate nullable: true
        amount scale: 2, validator: { val, obj, errors ->
            if ( val == null ) {
                return false
            }
            if (val < 0.0 && obj.operationType == OperationType.PAGAMENTO) {
                errors.rejectValue('amount', 'amount.invalid')
                return false
            }
            true
        }
        balance scale: 2, validator: { val, obj, errors ->
            if ( val == null ) {
                return false
            }
            if (val < 0.0 && obj.operationType == OperationType.PAGAMENTO) {
                errors.rejectValue('balance', 'balance.invalid')
                return false
            }
            true
        }
    }

    static mapping = {
        eventDate type: 'date'
        id generator: 'sequence', params: [sequence: 'transaction_seq']
        eventDate index: 'idx_transaction_eventDate'
    }
}
