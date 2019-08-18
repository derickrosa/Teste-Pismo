package enums

enum StatusTransaction {
    PURCHASED('Em aberto'),
    PAID('Pago')

    String nome

    StatusTransaction(nome){
        this.nome = nome
    }
}