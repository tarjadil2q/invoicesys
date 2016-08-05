<#-- @ftlvariable name="roles" type="java.util.List<com.pce.domain.Role>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>List of Roles</title>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/">Home</a></li>
        <li><a href="/role/create">Create a new role</a></li>
    </ul>
</nav>

<h1>List of Roles</h1>

<table>
    <thead>
    <tr>
        <th>Role Name</th>
    </tr>
    </thead>
    <tbody>
    <#list roles as role>
    <tr>
        <td><a href="/role/${role.id}">${role.roleName}</a></td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>