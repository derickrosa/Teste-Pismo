

<%=packageName%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <p>
            <g:link class="btn btn-default" action="index">
                <span class="glyphicon glyphicon-list"></span>
                <g:message code="default.list.label" args="[entityName]" />
            </g:link>
            <g:link class="btn btn-default" action="create">
                <span class="glyphicon glyphicon-plus"></span>
                <g:message code="default.new.label" args="[entityName]" />
            </g:link>
        </p>

        <div id="show-${propertyName}" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="\${flash.message}">
            <div class="message" role="status">\${flash.message}</div>
            </g:if>
            <f:display bean="${propertyName}" />

            <g:form url="[resource:${propertyName}, action:'delete']" method="DELETE">
                <g:link class="btn btn-default" action="edit" resource="\${${propertyName}}">
                    <span class="glyphicon glyphicon-edit"></span>
                    <g:message code="default.button.edit.label" default="Editar" />
                </g:link>
                <button type="submit" class="btn btn-default" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                    <span class="glyphicon glyphicon-floppy-remove"></span>
                    <g:message code="default.button.delete.label" default="Remover"/>
                </button>
            </g:form>
        </div>
    </body>
</html>
