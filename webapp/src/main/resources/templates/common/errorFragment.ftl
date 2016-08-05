<#macro showError userFormName>
    <#import "/spring.ftl" as spring>
    <@spring.bind userFormName />
    <#if spring.status.error>
    <ul>
        <#list spring.status.errorMessages as error>
            <li>${error}</li>
        </#list>
    </ul>
    </#if>
</#macro>