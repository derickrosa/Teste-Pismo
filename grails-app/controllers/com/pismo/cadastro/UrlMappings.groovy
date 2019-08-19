package com.pismo.cadastro

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"()

//        "/accounts"(resources:'account')

        '/transactions'(controller: 'transaction', action: 'save')
        '/payments'(controller: 'payment', action: 'save')
        '/accounts/limits'(controller: 'account', action: 'limits')
        "/accounts/$id"(controller: 'account', action: 'update')
    }
}
