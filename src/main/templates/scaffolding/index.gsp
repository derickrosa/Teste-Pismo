<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <p>
            <g:link class="btn btn-default" action="create">
                <span class="glyphicon glyphicon-plus"></span>
                <g:message code="default.new.label" args="[entityName]" />
            </g:link>
        </p>

    <g:if test="\${${propertyName}Count > 0}">
        <div id="list-${propertyName}" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="\${flash.message}">
                <div class="message" role="status">\${flash.message}</div>
            </g:if>
            <f:table collection="\${${propertyName}List}" />

            <div class="pagination">
                <g:paginate total="\${${propertyName}Count ?: 0}" />
            </div>
        </div>
    </g:if>
    <g:else>
        <pesquisa:dadosNaoEncontrados/>
    </g:else>
    </body>
</html>