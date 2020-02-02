package api.security


import static org.springframework.http.HttpStatus.*
import grails.gorm.transactions.Transactional

@Transactional(readOnly = true)
class RoleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [roleInstanceList: Role.list(params), roleInstanceCount: Role.count()]
    }

    def show(Role roleInstance) {
        [roleInstance: roleInstance]
    }

    def create() {
        [roleInstance: new Role(params)]
    }

    @Transactional
    def save(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }

        if (roleInstance.hasErrors()) {
            respond roleInstance.errors, view: 'create'
            return
        }

        roleInstance.nivelAcesso = Role.last().nivelAcesso + 1
        roleInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'role.label', default: 'Role'), roleInstance.id])
                redirect roleInstance
            }
            '*' { respond roleInstance, [status: CREATED] }
        }
    }

    def edit(Role roleInstance) {
        [roleInstance: roleInstance]
    }

    @Transactional
    def update(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }

        if (roleInstance.hasErrors()) {
            respond roleInstance.errors, view: 'edit'
            return
        }

        roleInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'role.label', default: 'Role'), roleInstance.id])
                redirect roleInstance
            }
            '*' { respond roleInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }

        if (UserRole.countByRole(roleInstance) > 0) {
            flash.error = "Existem usuário adastrados com essa autoridade."
            redirect(action: 'index', method: 'GET')
            return
        }

        roleInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'role.label', default: 'Role'), roleInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label', default: 'Role'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
