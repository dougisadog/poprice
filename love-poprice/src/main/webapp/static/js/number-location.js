$(document).ready(function(){
    if ($("#batchForm").size() > 0) {
        //点击tr选中改行checkbox
        $("#searchTable > tbody > tr").on("click",function(e){
            if (e.target.tagName && e.target.tagName.toUpperCase() == 'INPUT') {
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

        //删除全部
        $("#deleteAll").on("click", function () {
            numberLocationBatchDelete();
        });
    }

});

function numberLocationBatchDelete() {
    if(!confirm('确定删除这些数据吗？')) {
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