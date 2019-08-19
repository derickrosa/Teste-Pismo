package com.pismo.cadastro

import com.pismo.cadastro.OperationType
import com.pismo.security.Role
import com.pismo.security.User
import com.pismo.security.UserRole
import grails.gorm.transactions.Transactional


@Transactional
class FixtureService {

    def springSecurityService
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

    def setupUsers() {
        println("CRIANDO USUÁRIOS BÁSICOS...")
        def roleSuporte = new Role(authority: "ROLE_SUPORTE", nome: "Suporte", descricao: "Suporte Técnico do sistema. Possui controle total do sistema.", nivelAcesso: 0).save()
        def roleApi = new Role(authority: "ROLE_API", nome: "API", descricao: "Acessar a API. Possui acesso às APIs.", nivelAcesso: 1).save()

        def userSuporte = new User(nome: 'Pismo Soluções Tecnológicas', username: 'suporte.pismo', password: 'pismoadmin', email:'desenvolvimento@desenvolvimento.com.br', enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false).save()
        def userAPI = new User(nome: 'Usuário API', username: 'api.pismo', password: 'pismoapi', email:'api@api.com.br', enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false).save()

        UserRole.create userSuporte, roleSuporte, true
        UserRole.withSession {
            it.flush()
            it.clear()
        }
        UserRole.create userAPI, roleApi, true
        UserRole.withSession {
            it.flush()
            it.clear()
        }
    }
}
