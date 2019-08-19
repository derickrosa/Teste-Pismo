package testePismo

import com.pismo.cadastro.OperationType
import com.pismo.security.Role


class BootStrap {
    def fixtureService

    def init = { servletContext ->
        loadFixtures()
    }
    def destroy = {
    }

    void loadFixtures() {
        if (OperationType.count() == 0) fixtureService.setupOperationType()
        if (Role.count() == 0) fixtureService.setupUsers()
    }
}
