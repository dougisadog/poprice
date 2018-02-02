$(document).ready(function(){

    $("#search_EQ_spec, #search_EQ_product\\.spec").autocomplete(specList, {

        minChars: 0,
        width: 300,
        max:300,
        //matchContains: "word",
        autoFill: false,
        mustMatch:true,
        noRecord: "",
        //cacheLength: 0,
        formatItem: function(row, i, max) {
            return i + "/" + max + ": " + row;
        },
        formatMatch: function(row, i, max) {
            return row + "";
        },
        formatResult: function(row) {
            return row + "";
        }
    });

    $("#btnExport").click(function(e) {
        var searchForm = $("#searchForm");
        e.preventDefault();
        e.stopPropagation();
        var url = $(this).data("action");
        var _url = searchForm.attr("action");
        searchForm.attr("action", url);
        searchForm.submit();
        searchForm.attr("action", _url);
    });

    $("button.search").click(function() {
        var $this = $(this);
        var resultDiv = $("#" + $this.data("result-id"));
        var form = $this.parents("form");
        var action = form.attr("action");
        $.ajax({
            type: "post",
            url: action,
            cache: false,
            data: form.serialize(),
            dataType:'html',
            success: function(result){
                //alert(result);
                resultDiv.html(result);
                resultDiv.find("table").tablesorter({
                    // this will apply the bootstrap theme if "uitheme" widget is included
                    // the widgetOptions.uitheme is no longer required to be set
                    theme : "ice",
                    widthFixed: true,
                    headerTemplate : '{content} {icon}', // new in v2.7. Needed to add the bootstrap icon!
                    // widget code contained in the jquery.tablesorter.widgets.js file
                    // use the zebra stripe widget if you plan on hiding any rows (filter widget)
                    widgets : [ "uitheme", "zebra" ],
                    widgetOptions : {
                        // using the default zebra striping class name, so it actually isn't included in the theme variable above
                        // this is ONLY needed for bootstrap theming if you are using the filter widget, because rows are hidden
                        zebra : ["even", "odd"],
                        // reset filters button
                        filter_reset : ".reset",
                        // extra css class name (string or array) added to the filter element (input or select)
                        filter_cssFilter: "form-control",
                        // set the uitheme widget to use the bootstrap theme class names
                        // this is no longer required, if theme is set
                        // ,uitheme : "bootstrap"

                    }
                });
                //$("#leftPanel1,#rightPanel1").heightFix();
                $("#memberTableContainer,#productTableContainer").css('height', 'auto');
                $("#memberTableContainer,#productTableContainer").heightFix();
            }
        });

    });


    //点击tr选中改行checkbox
    $("#memberTableContainer, #productTableContainer").delegate("tbody>tr", "click",function(e){
        if (e.target.tagName && e.target.tagName.toUpperCase() == 'INPUT') {
            e.stopImmediatePropagation();
            return;
        }
        var checkbox = $(this).find("input[name=pid]");
        if (checkbox.size() < 1) {
            checkbox = $(this).find("input[name=mid]");
        }
        checkbox.prop("checked",!checkbox.prop("checked"));
    });
    //全选|反选
    $("#memberTableContainer, #memberTableContainer1").delegate(".check-all-customer", "click",function() {
        if ($(this).prop("checked")) {
            selectAllCheckbox($(this).parents(".table-container").find("input[name='mid']"));
        } else{
            cancelAllCheckbox($(this).parents(".table-container").find("input[name='mid']"));
        }
    });


    $("#productTableContainer").delegate("#checkedAllProduct", "click",function() {
        if ($(this).prop("checked")) {
            selectAllCheckbox("input[name='pid']");
        } else{
            cancelAllCheckbox("input[name='pid']");
        }
    });
    $("#productTableContainer").delegate("#checkedAllSpSms", "click",function() {
        if ($(this).prop("checked")) {
            selectAllCheckbox("input[name='spSms']");
        } else{
            cancelAllCheckbox("input[name='spSms']");
        }
    });
    $("#productTableContainer").delegate("#checkedAllCustomSms", "click",function() {
        if ($(this).prop("checked")) {
            selectAllCheckbox("input[name='customSms']");
        } else{
            cancelAllCheckbox("input[name='customSms']");
        }
    });

    //应用统一折扣
    $("#btnApplyDiscount").click(function() {
        clearMessage();

        var lbError = $("#discount-error");
        var _discount = $.trim($("#discount").val());
        if (_discount == '') {
            lbError.text("请输入折扣");
            return false;
        }
        if ($("table.product-table").size() == 0) {
            lbError.text("必须有可分配价格的产品");
            return false;
        }
        var discount = parseFloat(_discount);
        if (isNaN(discount) || discount < 1 || discount > 10) {
            lbError.text("请输入1-10的正确折扣值");
            return false;
        }
        $("table.product-table tbody>tr").each(function() {
            var tr = $(this);
            var basePrice = parseFloat(tr.find("td.base-price").text());
            var costPrice = parseFloat(tr.find("td.cost-price").text());
            //field-error
            var price = basePrice * discount / 10;
            var priceInput = tr.find("input[name=price]");
            priceInput.val(numeral(price).format('0[.]00'));
            if (price < costPrice || price > basePrice) {
                priceInput.addClass("field-error");
            } else {
                priceInput.removeClass("field-error");
            }
        });
    });
    $("#btnApplyLowest").click(function() {
        clearMessage();
        if ($("table.product-table").size() == 0) {
            lbError.text("必须有可分配价格的产品");
            return false;
        }

        $("table.product-table tbody>tr").each(function() {
            var tr = $(this);
            var costPrice = parseFloat(tr.find("td.cost-price").text());

            var priceInput = tr.find("input[name=price]");
            //priceInput.val(numeral(costPrice).format('0[.]00'));
            priceInput.val(costPrice);
            priceInput.removeClass("field-error");
        });
    });
    $("#btnApplyPriority").click(function() {
        clearMessage();
        var lbError = $("#priority-error");
        var _priority = $.trim($("#priority").val());
        if (_priority == '') {
            lbError.text("请输入优先级");
            return false;
        }
        if ($("table.product-table").size() == 0) {
            lbError.text("必须有可分配优先级的产品");
            return false;
        }
        var priority = parseInt(_priority);
        if (isNaN(priority)) {
            lbError.text("请输入正确的数字");
            return false;
        }
        $("table.product-table tbody>tr input[name=priority]").val(priority);
    });
    $("#btnCopySave").click(function() {
        clearMessage();
        var lbError = $("#copy-info");
        var srcCheckboxes = $("#leftPanel1 input[name='mid']:checked");
        if (srcCheckboxes.size() != 1) {
            lbError.text("请选择左侧的来源, 只能选中一个");
            return false;
        }
        var destCheckboxes = $("#rightPanel1 input[name='mid']:checked");
        if (destCheckboxes.size() == 0) {
            lbError.text("请选择右侧的目标, 可以选择多个");
            return false;
        }

        var fd = new FormData();
        srcCheckboxes.each(function () {
            var mid = $(this).val();
            fd.append("srcIds", mid);
        });
        destCheckboxes.each(function () {
            var mid = $(this).val();
            fd.append("destIds", mid);
        });

        if (!confirm("复制操作会清空目标客户所有已分配的产品，如果是分销商复制，同时会清空分销商的所有客户的分配的产品，确认要继续吗？")) {
            return false;
        }

        memberCopySave(fd, _saveAction);
    });


    $("#productTableContainer").delegate("input[name=price]", "input change blur", function() {
        var val = $(this).val();
        if (val != '' && !$.isNumeric(val)) {
            $(this).addClass("field-error");
            //alert("请填写数字");
            $(this).focus();
            return false;
        }
        var tr = $(this).parent().parent();
        var basePrice = parseFloat(tr.find("td.base-price").text());
        var costPrice = parseFloat(tr.find("td.cost-price").text());
        var price = parseFloat(val);

        if (price < costPrice || price > basePrice) {
            $(this).addClass("field-error");
        } else {
            $(this).removeClass("field-error");
        }
    });


    var lbAssignInfo = $("#assign-info");
    $("#btnAssign").click(function(e) {
        clearMessage();
        //数据校验
        var midList = $("input[name='mid']:checked");
        if (midList.size() < 1) {
            lbAssignInfo.text("请选择用户");
            return false;
        }
        var pidList = $("input[name='pid']:checked");
        if (pidList.size() < 1) {
            lbAssignInfo.text("请选择产品");
            return false;
        }

        //检查所有选择的产品,输入的价格必须是正确的
        var checkedOk = true;
        pidList.each(function() {
            var pidCheckbox = $(this);
            var tr = pidCheckbox.parent().parent();
            var basePrice = parseFloat(tr.find("td.base-price").text());
            var costPrice = parseFloat(tr.find("td.cost-price").text());
            var _input = tr.find("input[name=price]");
            var price = parseFloat(_input.val());

            if (isNaN(price) || price < costPrice || price > basePrice) {
                _input.addClass("field-error");
                checkedOk = false;
                return false;
            } else {
                checkedOk = true;
                _input.removeClass("field-error");
            }
        });
        if (!checkedOk) {
            lbAssignInfo.text("分配的产品价格有不正确的, 请检查");
            return false;
        }
        if(!confirm('确定要进行批量分配产品操作吗？')) {
            return false;
        }

        //收集数据,用formdata提交
        var fd = new FormData();
        midList.each(function() {
            fd.append("mid", $(this).val());
        });
        pidList.each(function() {
            var pidCheckbox = $(this);
            var tr = pidCheckbox.parent().parent();
            var _input = tr.find("input[name=price]");
            fd.append("pid", pidCheckbox.val());
            fd.append("price", _input.val());
            var spSms = tr.find("input[name=spSms]").is(":checked");
            var customSms = tr.find("input[name=customSms]").is(":checked");
            fd.append("spSms", spSms);
            fd.append("customSms", customSms);
            var _priority = tr.find("input[name=priority]").val();
            var priority = parseInt(_priority);
            if (isNaN(priority)) {
                priority = 100;
            }
            fd.append("priority", priority);
        });
        productBatchAssign(fd, _saveAction);
    });

    $("#btnAssignPriority").click(function(e) {
        clearMessage();
        //数据校验
        var midList = $("input[name='mid']:checked");
        if (midList.size() < 1) {
            lbAssignInfo.text("请选择用户");
            return false;
        }
        var pidList = $("input[name='pid']:checked");
        if (pidList.size() < 1) {
            lbAssignInfo.text("请选择产品");
            return false;
        }

        if(!confirm('确定要进行批量调整产品优先级的操作吗？')) {
            return false;
        }

        //收集数据,用formdata提交
        var fd = new FormData();
        midList.each(function() {
            fd.append("mid", $(this).val());
        });
        pidList.each(function() {
            var pidCheckbox = $(this);
            var tr = pidCheckbox.parent().parent();
            fd.append("pid", pidCheckbox.val());
            var spSms = tr.find("input[name=spSms]").is(":checked");
            var customSms = tr.find("input[name=customSms]").is(":checked");
            fd.append("spSms", spSms);
            fd.append("customSms", customSms);
            var _priority = tr.find("input[name=priority]").val();
            var priority = parseInt(_priority);
            if (isNaN(priority)) {
                priority = 100;
            }
            fd.append("priority", priority);
        });
        productBatchAssign(fd, _saveAction);
    });


    //下面全是index页面的
    //index页面的全选和反选
    $("#checkedAll").on("click",function() {
        if ($(this).prop("checked")) {
            selectAllCheckbox("input[name='ids']");
        } else{
            cancelAllCheckbox("input[name='ids']");
        }
    });
    $("#searchTable").delegate("tbody>tr", "click",function(e){
        if (e.target.tagName && (e.target.tagName.toUpperCase() == 'INPUT' || e.target.tagName.toUpperCase() == 'A')) {
            e.stopImmediatePropagation();
            return;
        }
        var checkbox = $(this).find("input[name=ids]");
        checkbox.prop("checked",!checkbox.prop("checked"));
    });
    //删除全部
    $("#removeAll").on("click", function () {
        memberProductBatchRemove();
    });


    function clearMessage() {
        $("#discount-error").empty();
        $("#priority-error").empty();
        $("#assign-info").empty();
        $("#copy-info").empty();
    }
});

function productBatchAssign(fd, url) {
    var lbAssignInfo = $("#assign-info");
    lbAssignInfo.text('');
    $("#btnAssign").button('loading');
    //alert(fd);
    $.ajax({
        type: "post",
        url: url,
        data: fd,
        cache: false,
        processData: false,
        contentType: false,
        dataType:'json',
        success: function(result){
            if (!result.ok) {
                showLabelError(lbAssignInfo, result.message);
                return false;
            } else{
                showLabelMessage(lbAssignInfo, result.message);
            }
        },
        complete: function() {
            $("#btnAssign").button('reset');
        }
    });
}

function memberProductBatchRemove() {
    if(!confirm('确定移除这些关联吗？')) {
        return;
    }
    var url = $("#batchForm").prop("action");
    var data = $("#batchForm").serialize();
    $.ajax({
        type: "post",
        url: url,
        data: data,
        dataType:'json',
        success: function(result){
            if (!result.ok) {
                alert(result.message);
                return false;
            } else{
                alert(result.message);
                location.reload();
            }
        }
    });
}

function memberCopySave(fd, url) {
    var lbCopyInfo = $("#copy-info");
    lbCopyInfo.text('');
    $("#btnCopySave").button('loading');
    $.ajax({
        type: "post",
        url: url,
        data: fd,
        cache: false,
        processData: false,
        contentType: false,
        dataType:'json',
        success: function(result){
            if (!result.ok) {
                showLabelError(lbCopyInfo, result.message);
                return false;
            } else{
                showLabelMessage(lbCopyInfo, result.message);
            }
        },
        complete: function() {
            $("#btnCopySave").button('reset');
        }
    });
}
