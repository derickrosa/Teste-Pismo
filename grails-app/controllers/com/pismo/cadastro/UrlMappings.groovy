package com.pismo.cadastro

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"()
        '/accounts'(controller: 'account', action: 'limits')
    }
}
