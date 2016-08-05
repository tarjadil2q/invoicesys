<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="userCreateForm" type="com.pce.domain.dto.UserCreationForm" -->
<#import "./common/errorFragment.ftl" as errorFragment>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create a new user</title>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/">Home</a></li>
    </ul>
</nav>

<h1>Create a new user</h1>
<@errorFragment.showError "userCreateForm"/>

<form role="userCreateForm" name="userCreateForm"  action="" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div>
        <label for="firstName">First Name</label>
        <input type="text" name="firstName" value="${userCreateForm.firstName}">
    </div>
    <div>
        <label for="lastName">Last Name</label>
        <input type="text" name="lastName" value="${userCreateForm.lastName}">
    </div>
    <div>
        <label for="email">Email address</label>
        <input type="email" name="email" id="email" value="${userCreateForm.email}" required autofocus/>
    </div>
    <div>
        <label for="password">Password</label>
        <input type="password" name="password" id="password"  required/>
    </div>
    <div>
        <label for="passwordRepeated">Repeat</label>
        <input type="password" name="passwordRepeated" id="passwordRepeated" required/>
    </div>
    <div>
        <label for="rolesName">Roles</label>
        <select name="selectedRoleIds" id="rolesName" required multiple>
            <#list userCreateForm.roles as role>
                <option value="${role.getId()}">${role.roleName}</option>
            </#list>
        </select>
    </div>
    <button type="submit">Save</button>
</form>



</body>
</html>