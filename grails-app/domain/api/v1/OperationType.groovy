package api.v1

class OperationType {
    String description
    Integer chargeOrder

    static constraints = {
        description blank: false, unique: true
    }

    static mapping = {
        id generator: 'assigned'
        description type:'text'
    }

    String toString() {
        description
    }

    static getAVISTA() {
        OperationType.get(1)
    }
    static getPARCELADA() {
        OperationType.get(2)
    }
    static getSAQUE() {
        OperationType.get(3)
    }
    static getPAGAMENTO() {
        OperationType.get(4)
    }
}
