package spring
import api.security.UserPasswordEncoderListener
// Place your Spring DSL code here
beans = {
    localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
        defaultLocale = new Locale("pt", "BR")
    }

    userPasswordEncoderListener(UserPasswordEncoderListener)
}
