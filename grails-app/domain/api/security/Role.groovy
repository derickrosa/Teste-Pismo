package api.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

	private static final long serialVersionUID = 1

	String authority
	String nome
	String descricao
	int nivelAcesso

	@Override
	int hashCode() {
		authority?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof Role && other.authority == authority)
	}

	@Override
	String toString() {
		authority
	}

	static constraints = {
		authority nullable: false, blank: false, unique: true
		nome nullable: false, blank: false
		descricao nullable: true, blank: true
		nivelAcesso nullable: false
	}

	static porNivelAcesso() {
		def listRoles = Role.createCriteria().list{
			order('nivelAcesso')
		}
		return listRoles
	}

	static mapping = {
		cache true
	}
}
