$(document).ready(function(){
    $("#spec, #search_EQ_spec, #search_EQ_product\\.spec").autocomplete(specList, {

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

    $("#areaType").change(function() {
        $("#province").off("change");
        var value = $(this).val();
        if (value == 'c') {
            //国内,这是改到国内的
            $.provinceCity($("#province"), $("#city"), true, false, true);
        } else {
            $.provinceCity($("#province"), $("#city"), false, false, true);
        }
    });

    $("#areaType").trigger("change");

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

    //批量改价
    $("#btnBatchUpdatePrice").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量改价的产品");
            return;
        }

        var costPriceDiscountVal = $("#costPriceDiscount").val();
        var agentPriceDiscountVal = $("#agentPriceDiscount").val();
        var memberPriceDiscountVal = $("#memberPriceDiscount").val();
        var retailPriceDiscountVal = $("#retailPriceDiscount").val();

        var costPriceDiscount = null;
        if (costPriceDiscountVal != '') {
            costPriceDiscount = parseFloat(costPriceDiscountVal);
            if (isNaN(costPriceDiscount) || costPriceDiscount < 1 || costPriceDiscount > 10) {
                alert("成本价折扣必须是1-10的数字");
                return false;
            }
        }
        var agentPriceDiscount = null;
        if (agentPriceDiscountVal != '') {
            agentPriceDiscount = parseFloat(agentPriceDiscountVal);
            if (isNaN(agentPriceDiscount) || agentPriceDiscount < 1 || agentPriceDiscount > 10) {
                alert("代理价折扣必须是1-10的数字");
                return false;
            }
            if (costPriceDiscount != null && agentPriceDiscount != null) {
                if (costPriceDiscount >= agentPriceDiscount) {
                    alert("代理价折扣必须大于成本价折扣");
                    return false;
                }
            }
        }
        var memberPriceDiscount = null;
        if (memberPriceDiscountVal != '') {
            memberPriceDiscount = parseFloat(memberPriceDiscountVal);
            if (isNaN(memberPriceDiscount) || memberPriceDiscount < 1 || memberPriceDiscount > 10) {
                alert(memberPriceDiscount);
                alert("会员价折扣必须是1-10的数字");
                return false;
            }
            if (memberPriceDiscount != null && agentPriceDiscount != null) {
                if (agentPriceDiscount >= memberPriceDiscount) {
                    alert("会员价折扣必须大于代理价折扣");
                    return false;
                }
            }
            if (memberPriceDiscount != null && costPriceDiscount != null) {
                if (costPriceDiscount >= memberPriceDiscount) {
                    alert("会员价折扣必须大于成本价折扣");
                    return false;
                }
            }
        }
        var retailPriceDiscount = null;
        if (retailPriceDiscountVal != '') {
            retailPriceDiscount = parseFloat(retailPriceDiscountVal);
            if (isNaN(retailPriceDiscount) || retailPriceDiscount < 1 || retailPriceDiscount > 10) {
                alert("零售价折扣必须是1-10的数字");
                return false;
            }
            if (memberPriceDiscount != null && retailPriceDiscount != null) {
                if (memberPriceDiscount >= retailPriceDiscount) {
                    alert("零售价折扣必须大于会员价折扣");
                    return false;
                }
            }
            if (agentPriceDiscount != null && retailPriceDiscount != null) {
                if (agentPriceDiscount >= retailPriceDiscount) {
                    alert("零售价折扣必须大于代理价折扣");
                    return false;
                }
            }
            if (costPriceDiscount != null && retailPriceDiscount != null) {
                if (costPriceDiscount >= retailPriceDiscount) {
                    alert("零售价折扣必须大于成本价折扣");
                    return false;
                }
            }
        }

        if (!confirm("您确认要进行批量修改价格操作吗(该操作可能耗费较长时间)?")) {
            return false;
        }

        var searchForm = $("#searchForm");
        e.preventDefault();
        e.stopPropagation();
        var url = $(this).data("action");
        var _url = searchForm.attr("action");
        searchForm.attr("action", url);
        $("#appenderDiv").append($("input[name='ids']:checked"));
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

    $("#btnBatchUpdateStatus1").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量上架的产品");
            return;
        }
        if(!confirm('确定要进行批量上架操作吗？')) {
            return;
        }
        productBatchChangeStatus('avail', $("#batchForm").data("action-status"));
    });

    $("#btnBatchUpdateStatus2").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量下架的产品");
            return;
        }
        if(!confirm('确定要进行批量下架操作吗？')) {
            return;
        }
        productBatchChangeStatus('unavail', $("#batchForm").data("action-status"));
    });

    $("#btnBatchUpdateProcessor").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量更换接口的产品");
            return;
        }
        var processorId = $("#processorId").val();
        if (processorId == "") {
            alert("请选择要批量更换的目标接口");
            return;
        }
        if(!confirm('确定要进行批量更换接口的操作吗？')) {
            return;
        }

        productBatchChangeProcessor(processorId, $("#batchForm").data("action-processor"), false);
    });
    $("#btnBatchUpdateProcessor2").click(function(e) {
        if($("input[name='ids']:checked").size() < 1) {
            alert("请选择要批量更换备用接口的产品");
            return;
        }
        var processorId = $("#processorId").val();
        if (processorId == "") {
            if(!confirm('确定要把这些产品的备用接口都设为空吗？')) {
                return;
            }
        } else {
            if(!confirm('确定要进行批量更换备用接口的操作吗？')) {
                return;
            }
        }

        productBatchChangeProcessor(processorId, $("#batchForm").data("action-processor"), true);
    });
});

function productBatchChangeStatus(status, url) {
    var data = $("#batchForm").serialize();
    data = data + "&status=" + status;
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

function productBatchChangeProcessor(processorId, url, backup) {
    var data = $("#batchForm").serialize();
    data = data + "&processorId=" + processorId + "&backup=" + backup;
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