<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" layout:decorator="layouts/main">
<head lang="zh">
    <title>详情</title>
</head>
<body>
<div class="col-md-10" layout:fragment="content">
    <div class="widget-content">
        <h2>
            <span>详情</span>
            <a class="back" th:href="@{${mapping}/list}" href="#">返回 &gt;&gt;</a>
        </h2>

        <div class="main-panel">
            <div class="detail-panel">
            <#list parameterMap?keys as key>
                <div class="row">
                    <label class="col-md-1 control-label">${key}:</label>
                    <div class="col-md-4" th:text="${"$"}{entity.${parameterMap[key]}}"></div>
                </div>
            </#list>
                <div class="row">
                    <label class="col-md-2 control-label">创建人:</label>
                    <div class="col-md-2" th:text="${"$"}{entity.createdBy}"></div>

                    <label class="col-md-2 control-label">创建时间:</label>
                    <div class="col-md-2" th:text="${"$"}{${"#"}dates.format(entity.createdDate, ${"#"}messages.msg('app.datetime_format'))}"></div>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label">最后修改人:</label>
                    <div class="col-md-2" th:text="${"$"}{entity.lastModifiedBy}"></div>
                    <label class="col-md-2 control-label">最后修改时间:</label>
                    <div class="col-md-2" th:text="${"$"}{${"#"}dates.format(entity.lastModifiedDate, ${"#"}messages.msg('app.datetime_format'))?:''}" th:if="${"$"}{entity.lastModifiedDate!=null}"></div>
                </div>
                <div class="row">
                    <div class="col-md-7 button-panel">
                        <a id="btnEdit" class="btn btn-primary" th:href="@{${mapping}/edit/{id}(id=${"$"}{entity.id})}">编辑</a>
                        <a id="btnDelete" class="btn btn-danger delete" th:href="@{${mapping}/delete/{id}(id=${"$"}{entity.id})}">删除</a>
                        <a id="btnBack"  th:href="@{${mapping}/list?{params}(params=${"$"}{params})}" class="btn btn-default">返回</a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>