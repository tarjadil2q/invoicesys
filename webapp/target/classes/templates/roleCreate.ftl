<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="roleCreateForm" type="com.pce.domain.dto.RoleCreationForm" -->
<#import "./common/errorFragment.ftl" as errorFragment>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create a new role</title>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/">Home</a></li>
    </ul>
</nav>

<h1>Create a new role</h1>
<@errorFragment.showError "roleCreateForm"/>

<form role="roleCreateForm" name="roleCreateForm"  action="" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div>
        <label for="roleName">Role Name</label>
        <input type="text" name="roleName" value="${roleCreateForm.roleName}">
    </div>
    <button type="submit">Save</button>
</form>


</body>
</html>