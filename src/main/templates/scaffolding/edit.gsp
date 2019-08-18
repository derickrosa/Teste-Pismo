<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <p>
            <g:link class="btn btn-default" action="index">
                <span class="glyphicon glyphicon-list"></span>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
            <g:link class="btn btn-default" action="create">
                <span class="glyphicon glyphicon-plus"></span>
                <g:message code="default.new.label" args="[entityName]"/>
            </g:link>
        </p>

        <div class="row">
            <div class="col-lg-12">
                <g:form url="[resource:${propertyName}, action:'update']">
                    <g:hiddenField name="version" value="\${${propertyName}?.version}"/>
                    <g:render template="form"></g:render>

                    <button type="submit" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit"></span>
                        <g:message code="default.button.update.label" default="Atualizar"/>
                    </button>
                </g:form>
            </div>
        </div>
    </body>
</html>
