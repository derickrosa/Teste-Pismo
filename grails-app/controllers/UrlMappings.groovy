class UrlMappings {

    static mappings = {
        "/"(controller: 'home', action: 'index')
        "/$controller/$action?/$id?(.$format)?"()


        "/accounts"(resources:'account')
    }
}
