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
        amount scale: 2
        balance scale: 2
        dueDate nullable: true
    }

    static mapping = {
        eventDate type: 'date'
        id generator: 'sequence', params: [sequence: 'transaction_seq']
        eventDate index: 'idx_transaction_eventDate'
    }
}
