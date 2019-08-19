/**
 * Created by andersonmarques on 04/10/17.
 */

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/'
grails.plugin.springsecurity.adh.errorPage = '/errors/forbidden'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.pismo.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.pismo.security.UserRole'
grails.plugin.springsecurity.authority.className = 'com.pismo.security.Role'
grails.plugin.springsecurity.useSwitchUserFilter = true
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.basic.realmName = "Teste Pismo"

grails.plugin.springsecurity.useSecurityEventListener = true
grails.plugin.springsecurity.onAbstractAuthenticationFailureEvent = { e, appCtx ->
	println "\nERROR auth failed for user $e.authentication.name: $e.exception.message\n"
}


grails.plugin.console.enabled = true

grails.databinding.dateFormats = ['dd/MM/yyyy', 'yyyy-MM-dd HH:mm:ss.S', "yyyy-MM-dd'T'hh:mm:ss'Z'"]
grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_SUPORTE > ROLE_API
'''

grails.plugin.springsecurity.filterChain.chainMap = [
	//Traditional chain
	[pattern: '/mensagem/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter'],
	[pattern: '/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter,-basicAuthenticationFilter,-basicExceptionTranslationFilter']
]

grails.plugin.springsecurity.interceptUrlMap = [
        [pattern: '/',                      access: ['permitAll']],
        [pattern: '/assets/**',             access: ['permitAll']],
        [pattern: '/**/js/**',              access: ['permitAll']],
        [pattern: '/**/css/**',             access: ['permitAll']],
        [pattern: '/**/images/**',          access: ['permitAll']],
        [pattern: '/**/favicon.ico',        access: ['permitAll']],
        [pattern: '/login/**',              access: ['permitAll']],
        [pattern: '/logout/**',             access: ['permitAll']],
		[pattern: '/transaction/**',        access: ['ROLE_API']],
		[pattern: '/account/**',        	access: ['ROLE_API']],
		[pattern: '/accounts/**',        	access: ['ROLE_API']],
		[pattern: '/transactions/**',       access: ['ROLE_API']],
		[pattern: '/payment/**',            access: ['ROLE_API']],
		[pattern: '/payments/**',           access: ['ROLE_API']],
		[pattern: '/console/**',        	access: ['ROLE_SUPORTE']],
		[pattern: '/static/console/**',     access: ['ROLE_SUPORTE']]

]

