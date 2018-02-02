<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"  layout:decorator="layouts/main">

<head lang="zh">
    <title>管理</title>
    <link rel="stylesheet" type="text/css" media="screen" href="../../../../static/css/tablesorter.css"
          th:href="@{/static/css/tablesorter.css}"/>
    <script src="../../../../static/js/traffic-table.js" th:src="@{/static/js/traffic-table.js}"></script>
    <script src="../../../../static/js/my97/WdatePicker.js" th:src="@{/static/js/my97/WdatePicker.js}"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#searchTable").searchTable();
        });
    </script>
</head>
<body>

<div class="col-md-10" layout:fragment="content">
    <div class="widget-content">
        <h2>
            <span>管理</span>

            <div class="operation" sec:authorize="hasAnyRole('')">
                <span>
                    <a href="#" class="btn btn-sm btn-default" th:href="@{${mapping}/create}">+ 新增</a>
                </span>
            </div>

        </h2>

        <form id="searchForm" action="#" method="get" th:action="@{${mapping}/list}" class="form-inline space-well">
            <div th:include="layouts/search-form :: search-params" th:remove="tag"></div>



            <div class="form-group form-group-sm">
                <button type="submit" class="btn btn-default btn-sm">搜索</button>
            </div>

        </form>

        <div class="well well-sm" th:inline="text">
            共[[${"$"}{list.size()}]]条数据
        </div>
        <div class="message" th:include="layouts/message-template :: content" th:if="${"$"}{#vars['flash.message'] != null or #vars['flash.error'] != null}" th:remove="tag"></div>

        <table id="searchTable" class="table table-hover table-striped tablesorter-bootstrap">
            <thead>
            <tr>
                <#list parameterMap?keys as key>
                <th class="tablesorter-header" data-prop="${key}">${key}</th>
                </#list>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="r,stat : ${"$"}{list}">
            <#list parameterMap?keys as key>
                <td th:text="${"$"}{r.${parameterMap[key]}}">${parameterMap[key]}</td>
            </#list>
                <td>
                    <a href="#" th:href="@{${mapping}/show/{id}(id=${"$"}{r.id})}">详细</a>
                    &nbsp;|
                    <a href="#" th:href="@{${mapping}/edit/{id}(id=${"$"}{r.id})}">编辑</a>
                    &nbsp;|
                    <a class="delete" href="#" th:href="@{${mapping}/delete/{id}(id=${"$"}{r.id})}">删除</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>