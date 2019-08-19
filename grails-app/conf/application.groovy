/**
 * Created by andersonmarques on 04/10/17.
 */

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/home/painelInicial'
grails.plugin.springsecurity.adh.errorPage = '/errors/forbidden'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'UserRole'
grails.plugin.springsecurity.authority.className = 'Role'
grails.plugin.springsecurity.useSwitchUserFilter = true
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.basic.realmName = "Gestão Fundo Dema"
/*grails.plugin.console.baseUrl = "http://35.198.16.201/testePismo"*/

grails.plugin.console.enabled = true //habilita o console para todos os enviroments

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
		[pattern: '/transaction/**',        access: ['permitAll']],
		[pattern: '/transactions/**',       access: ['permitAll']],
		[pattern: '/payment/**',            access: ['permitAll']],
		[pattern: '/payments/**',           access: ['permitAll']],
		[pattern: '/console/**',        	access: ['ROLE_SUPORTE']],
		[pattern: '/static/console/**',     access: ['ROLE_SUPORTE']]

]

