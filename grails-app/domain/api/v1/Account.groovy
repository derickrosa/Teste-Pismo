package api.v1

class Account {
    BigDecimal availableCreditLimit = 0.0
    BigDecimal availableWithdrawalLimit = 0.0

    static hasMany=[transactions: Transaction]

    static constraints = {
        availableCreditLimit scale: 2
        availableWithdrawalLimit scale: 2
    }

    void addAvailableCreditLimit(BigDecimal amount){
        this.availableCreditLimit += amount
        this.save()
    }

    void addAvailableWithdrawalLimit(BigDecimal amount){
        this.availableWithdrawalLimit += amount
        this.save()
    }
}
