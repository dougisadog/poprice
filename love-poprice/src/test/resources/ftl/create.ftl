<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" layout:decorator="layouts/main">

<head lang="zh">
    <title>新增</title>
    <script src="../../../../static/js/jquery-validation/jquery.validate.min.js" th:src="@{/static/js/jquery-validation/jquery.validate.min.js}"></script>
    <script src="../../../../static/js/jquery-validation/localization/messages_zh.min.js" th:src="@{/static/js/jquery-validation/localization/messages_zh.min.js}"></script>
    <script src="../../../../static/js/my97/WdatePicker.js" th:src="@{/static/js/my97/WdatePicker.js}"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#inputForm").validate();
        });
    </script>
</head>
<body>
<div class="col-md-10" layout:fragment="content" th:object="${"$"}{entity}">
    <div class="widget-content">
        <h2>
            <span>新增</span>
        </h2>
        <div class="message" th:include="layouts/message-template :: content" th:if="${"$"}{#vars['flash.message'] != null or #vars['flash.error'] != null}" th:remove="tag"></div>
        <form id="inputForm" action="#" th:action="@{${mapping}/save}" method="post" class="form-horizontal" th:object="${"$"}{entity}">
            <div class="form-body" th:include="${mapping}/form :: content"></div>
            <div class="row">
                <div class="col-md-7 button-panel">
                    <button id="btnCreate" class="btn btn-success">提交</button>
                    <a id="btnBack"  th:href="@{${mapping}/list}" class="btn btn-default">返回</a>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>