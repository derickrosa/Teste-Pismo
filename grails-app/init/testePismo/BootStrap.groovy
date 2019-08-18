package testePismo

import com.pismo.cadastro.OperationType


class BootStrap {
    def fixtureService

    def init = { servletContext ->
        loadFixtures()
    }
    def destroy = {
    }

    void loadFixtures() {
        if (OperationType.count() == 0) fixtureService.setupOperationType()
    }
}
