package api.v1

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"()

//        "/accounts"(resources:'account')

        '/v1/transactions'(controller: 'transaction', action: 'save')
        '/v1/payments'(controller: 'payment', action: 'save')
        '/v1/accounts/limits'(controller: 'account', action: 'limits')
        "/v1/accounts/$id"(controller: 'account', action: 'update')
        "/v1/accounts"(controller: 'account', action: 'save')
    }
}
