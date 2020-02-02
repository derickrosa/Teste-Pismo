package api.v1


@grails.persistence.Entity
class Transaction {

    BigDecimal amount
    BigDecimal balance
    Date eventdate = new Date()
    Date dueDate

    OperationType operationType

    static belongsTo = [account: Account]


    static constraints = {
        amount scale: 2
        balance scale: 2
        dueDate nullable: true
    }

    static mapping = {
        eventdate type: 'date'
        id generator: 'sequence', params: [sequence: 'transaction_seq']
        eventdate index: 'idx_transaction_eventDate'
    }
}
