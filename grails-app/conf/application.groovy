grails.plugin.console.enabled = true
grails.databinding.dateFormats = ['dd/MM/yyyy', 'yyyy-MM-dd HH:mm:ss.S', "yyyy-MM-dd'T'hh:mm:ss'Z'"]

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'api.security.User'
grails.plugin.springsecurity.authority.className = 'api.security.Role'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'api.security.UserRole'
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.password.algorithm = 'SHA-256'

grails.plugin.springsecurity.rest.token.validation.useBearerToken = false
grails.plugin.springsecurity.rest.login.endpointUrl = '/login'
grails.plugin.springsecurity.rest.logout.endpointUrl = '/logout'
grails.plugin.springsecurity.rest.token.validation.endpointUrl = '/validate'
grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'

grails.plugin.springsecurity.filterChain.chainMap = [
        [
                pattern: '/**',
                filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
        ]
]

grails.plugin.springsecurity.interceptUrlMap = [
        [pattern: '/**', access: ['isFullyAuthenticated()']],
        [pattern: '/login', access: ['permitAll']],
        [pattern: '/logout', access: ['permitAll']]
]