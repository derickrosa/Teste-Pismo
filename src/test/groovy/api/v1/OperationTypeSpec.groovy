package api.v1

import grails.testing.gorm.DomainUnitTest
import spock.lang.Shared
import spock.lang.Specification

class OperationTypeSpec extends Specification implements DomainUnitTest<OperationType> {
    @Shared int id

    void "test basic persistence mocking"() {
        setup:
        def operationTypeDebit = new OperationType(id: 1, description: 'COMPRA A VISTA', chargeOrder: 2)
        operationTypeDebit.id = 1
        operationTypeDebit.save(flush: true)

        def operationTypeWithdrawal = new OperationType(description: 'SAQUE', chargeOrder: 0)
        operationTypeWithdrawal.id = 3
        operationTypeWithdrawal.save(flush: true)

        new OperationType(description: 'SAQUE', chargeOrder: 0).save(flush: true)

        expect:
        OperationType.count() == 2
    }

    void "test domain instance"() {
        setup:
        id = System.identityHashCode(domain)

        expect:
        domain != null
        domain.hashCode() == id

        when:
        domain.description = 'COMPRA A VISTA'

        then:
        domain.description == 'COMPRA A VISTA'
    }

    void "test we get a new domain"() {
        expect:
        domain != null
        domain.description == null
        domain.chargeOrder == null
        System.identityHashCode(domain) != id
    }

    void 'test description cannot be null'() {
        when:
        domain.description = null

        then:
        !domain.validate(['description'])
        domain.errors['description'].code == 'nullable'
    }

    void 'test description cannot be blank'() {
        when:
        domain.description = ''

        then:
        !domain.validate(['description'])
        domain.errors['description'].code == 'blank'
    }

    void 'test chargeOrder cannot be null'() {
        when:
        domain.chargeOrder = null

        then:
        !domain.validate(['chargeOrder'])
        domain.errors['chargeOrder'].code == 'nullable'
    }

    void "test getAVISTA"() {
        setup:
        def operationType = new OperationType(id: 1, description: 'COMPRA A VISTA', chargeOrder: 2)
        operationType.id = 1
        operationType.save(flush: true)

        expect:
        OperationType.AVISTA == operationType
    }

    void "test getPARCELADA"() {
        setup:
        def operationType = new OperationType(description: 'COMPRA PARCELADA', chargeOrder: 1)
        operationType.id = 2
        operationType.save()

        expect:
        OperationType.PARCELADA == operationType
    }

    void "test getSAQUE"() {
        setup:
        def operationType = new OperationType(description: 'SAQUE', chargeOrder: 0)
        operationType.id = 3
        operationType.save()

        expect:
        OperationType.SAQUE == operationType
    }

    void "test getPAGAMENTO"() {
        setup:
        def operationType = new OperationType(description: 'PAGAMENTO', chargeOrder: 0)
        operationType.id = 4
        operationType.save()

        expect:
        OperationType.PAGAMENTO == operationType
    }

    def "operationType's description unique constraint"() {

        when: 'You instantiate a operationType with description which has been never used before'
        def operationType = new OperationType(description: 'SAQUE', chargeOrder: 0)
        operationType.id = 3
        operationType.save(flush: true)

        then: 'operationType is valid instance'
        operationType.validate()

        and: 'we can save it, and we get back a not null GORM Entity'
        operationType.save()

        and: 'there is one additional OperationType'
        OperationType.count() == old(OperationType.count()) + 1

        when: 'instanting a different operationType with the same description'
        def withDrawal = new OperationType(description: 'SAQUE', chargeOrder: 0)

        then: 'the operationType instance is not valid'
        !withDrawal.validate(['description'])

        and: 'unique error code is populated'
        withDrawal.errors['description']?.code == 'unique'

        and: 'trying to save fails too'
        !withDrawal.save()

        and: 'no operationType has been added'
        OperationType.count() == old(OperationType.count())
    }
}
