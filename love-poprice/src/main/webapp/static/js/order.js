$(document).ready(function(){
    $("#spec, #search_EQ_spec").autocomplete(specList, {

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
    //条件导出
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

    //点击tr选中改行checkbox
    $("#searchTable > tbody > tr").on("click",function(e){
        if (e.target.tagName && (e.target.tagName.toUpperCase() == 'INPUT' || e.target.tagName.toUpperCase() == 'A')) {
            e.stopImmediatePropagation();
            return;
        }
        $(this).find("input[type=checkbox]").prop("checked",!$(this).find("input[type=checkbox]").prop("checked"));
    });

    //全选|反选
    $("#checkedAll").on("click",function() {
        if ($(this).prop("checked")) {
            selectAllCheckbox("input[name='ids']");
        } else{
            cancelAllCheckbox("input[name='ids']");
        }
    });

    $("#btnBatchRefund").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量退款的订单");
            return;
        }
        if(!confirm('确定要进行批量退款操作吗？')) {
            return;
        }
        orderBatchRefund($("#batchForm").data("action-refund"));
    });

    $("a.confirm").click(function(e) {
        //e.stopPropagation();
        if(!confirm('确定要进行到账操作吗？')) {
            return false;
        }
        return true;
    });
    $("a.refund-balance").click(function(e) {
        //e.stopPropagation();
        if(!confirm('确定要进行退款操作吗？')) {
            return false;
        }
        return true;
    });
    $("a.refund-alipay").click(function(e) {
        //e.stopPropagation();
        if(!confirm('确定要退款到支付宝吗？')) {
            return false;
        }
        return true;
    });
    $("#btnBatchConfirm").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量到账的订单");
            return;
        }
        if(!confirm('确定要进行批量到账操作吗？')) {
            return;
        }
        orderBatchConfirm($("#batchForm").data("action-confirm"));
    });

    $("#btnBatchToGet").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量重新提取的订单, 状态只能是正在送冲");
            return;
        }
        if(!confirm('确定要进行批量重新提取操作吗？')) {
            return;
        }
        orderBatchToGet($("#batchForm").data("action-toget"));
    });

});

function orderBatchRefund(url) {

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

function orderBatchConfirm(url) {
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

function orderBatchToGet(url) {
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