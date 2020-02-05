package api.security

import api.security.User
import  api.security.Role
import grails.plugin.springsecurity.SpringSecurityUtils

import static org.springframework.http.HttpStatus.*

class UserController {
    def springSecurityService
    def userService

    static allowedMethods = [save: "POST", update: "PUT", changePassword: "POST", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def userList
        def userCount

        def criteria = {

        }
        userList = User.createCriteria().list(params, criteria)
        userCount = User.createCriteria().count(criteria)
        [userInstanceList: userList, userInstanceCount: userCount]
    }

    def show(User userInstance) {
        [userInstance: userInstance]
    }

    def create() {
        def roleList = Role.porNivelAcesso()
        if (SpringSecurityUtils.ifNotGranted("ROLE_SUPORTE")) roleList.removeAll { it.nivelAcesso == 0 }

        [userInstance: new User(params), authorityList: roleList]
    }

    def edit(User userInstance) {
        def roleList = Role.porNivelAcesso()
        if (SpringSecurityUtils.ifNotGranted("ROLE_SUPORTE")) roleList.removeAll { it.nivelAcesso == 0 }

        [userInstance: userInstance, authorityList: roleList]
    }

    def save(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            user = userService.save(user, params.list('roles'))
            flash.message = "Usuário ${user.nome} criado."
            redirect(action: 'show', id: user.id)
        }
        catch (Exception e) {
            e.printStackTrace()
            flash.error = "Um erro ocorreu."
            redirect(action: 'create')
        }
    }

    def update() {
        User user = User.get(params.long('id'))
        if (user == null) {
            notFound()
            return
        }
        long version = params.long('version')
        if (version != null && user.version > version) {
            flash.error = "Operação não permitida. Outro usuário do sistema tentou alterar essa instância."
            redirect(action: 'index')
            return
        }

        try {
            user.properties = params
            user = userService.save(user, params.list('roles'))
            flash.message = "Usuário ${user.nome} alterado."
            redirect(action: 'show', id: user.id)
        }
        catch (Exception e) {
            e.printStackTrace()
            flash.error = "Um erro ocorreu."
            redirect(action: 'edit', params: params)
        }
    }

    def delete(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        userService.delete(userInstance)
        flash.message = "Usuário deletado."
        redirect(action: 'index')
    }

    def changePassword() {
        User user = User.get(params.long('id'))
        String senha = params['senha']
        String confirmarSenha = params['confirmarSenha']

        if (user == null || senha?.isEmpty() || confirmarSenha?.isEmpty()) {
            response.status = NOT_ACCEPTABLE.value()
            [error: "Parâmetros insuficientes."]
            return
        } else if (!senha.equals(confirmarSenha)) {
            response.status = NOT_ACCEPTABLE.value()
            [error: "Senhas são diferentes."]
            return
        }

        user.password = senha
        user.save()

        response.status = OK.value()
        [msg: 'Senha alterada com sucesso']
    }

    def meuUsuario() {
        redirect(action: 'show', id: springSecurityService.currentUser?.id)
    }

    def isSamePassword() {
        def user = User.get(params.long('userID'))
        respond([isSamePassword: springSecurityService.passwordEncoder.isPasswordValid(user.password, params.password.toString(), null)])
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
