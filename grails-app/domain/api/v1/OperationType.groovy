package api.v1

class OperationType {
    String description
    Integer chargeOrder

    static constraints = {
        description blank: false, unique: true
    }

    static mapping = {
        description type:'text'
    }

    String toString() {
        description
    }

    static getAVISTA() {
        OperationType.findByDescription('COMPRA A VISTA')
    }
    static getPARCELADA() {
        OperationType.findByDescription('COMPRA PARCELADA')
    }
    static getSAQUE() {
        OperationType.findByDescription('SAQUE')
    }
    static getPAGAMENTO() {
        OperationType.findByDescription('PAGAMENTO')
    }
}
