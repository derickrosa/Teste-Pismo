package com.pismo.cadastro

class OperationType {
    Long id
    String name
    String description
    Integer chargeOrder
    Boolean ativo = true

    static constraints = {
        name blank: false
        description blank: false
    }

    static mapping = {
        id generator: 'assigned'
        description type:'text'
    }

    String toString() {
        name
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
