import com.pismo.cadastro.OperationType
import grails.gorm.transactions.Transactional


@Transactional
class FixtureService {
    def setupOperationType() {
        log.debug("Criando instâncias pré-configuradas de com.pismo.cadastro.OperationType")
        if (!OperationType.get(1)) {
            def operationType=new OperationType(name:'À vista',description:'Transação à vista',chargeOrder: 2)
            operationType.id=1
            operationType.save(failOnError:true, flush:true)
        }
        if (!OperationType.get(2)) {
            def operationType=new OperationType(name:'Parcelada',description:'Transação parcelada',chargeOrder: 1)
            operationType.id=2
            operationType.save(failOnError:true, flush:true)
        }
        if (!OperationType.get(3)) {
            def operationType=new OperationType(name:'Saque',description:'Transação de saque',chargeOrder: 0)
            operationType.id=3
            operationType.save(failOnError:true, flush:true)
        }
        if (!OperationType.get(4)) {
            def operationType=new OperationType(name:'Pagamento',description:'Transação de Pagamento',chargeOrder: 0)
            operationType.id=4
            operationType.save(failOnError:true, flush:true)
        }
    }
}
