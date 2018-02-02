<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head lang="zh">
</head>
<body>
<div class="form-body" th:fragment="content">
    <input type="hidden" name="id" th:value="*{id}" />

    <#list parameterMap?keys as key>
    <div class="form-group">
        <label class="col-md-1 control-label" for="${parameterMap[key]}">${key}<span class="indicator">*</span>:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" data-rule-required="true" th:field="*{${parameterMap[key]}}" th:errorclass="field-error" />
            <label class="error" th:if="${"$"}{#fields.hasErrors('${parameterMap[key]}')}" th:errors="*{${parameterMap[key]}}"></label>
        </div>
    </div>
    </#list>
</div>
</body>
</html>