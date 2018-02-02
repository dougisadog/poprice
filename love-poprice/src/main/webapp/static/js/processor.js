$(document).ready(function(){
    //点击tr选中改行checkbox
    $("#searchTable > tbody > tr").on("click",function(){
        $(this).find("input[type=checkbox]").prop("checked",!$(this).find("input[type=checkbox]").prop("checked"));
    });

    //由于点击tr中得checkbox 同时也选中了tr 所有需要再次反选一下
    $("input[name='ids']").on("click",function(){
        $(this).prop("checked", !$(this).prop("checked"));
    });

    //全选|反选
    $("#searchTable").delegate("#checkedAll", "click",function() {
        if ($(this).prop("checked")) {
            cancelAllCheckbox("input[name='ids']:hidden");
            selectAllCheckbox("input[name='ids']:visible");
        } else{
            cancelAllCheckbox("input[name='ids']:visible");
        }
    });

    //删除全部
    $("#btnBatchDeleteProcessorDetails").on("click", function(e) {
        e.preventDefault();
        if(!confirm('确定删除这些数据吗？')) {
            return;
        }
        var url = $(this).prop("href");
        var data = $("#inputForm").serialize();
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
    });

    //批量保存
    $("#btnBatchSaveProcessorDetails").on("click", function(e) {
        e.preventDefault();
        if(!confirm('确定保存这些数据吗？')) {
            return;
        }
        var url = $(this).prop("href");
        var data = $("#inputForm").serialize();
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
    });

});

