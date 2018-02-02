$(document).ready(function(){
    //业务员关系绑定
    $("#agent").change(function() {
        var $this = $(this);
        var val = $this.val();

        if (val == '') {
            $("#salesperson").removeAttr("disabled");
        } else {
            var sid = $this.find("option:selected").data("salesperson-id");
            if (sid != null && sid != '') {
                $("#salesperson option").removeAttr("selected");
                $("#salesperson option[value='" + sid + "']").attr("selected", "selected");
                $("#salesperson").attr("disabled", "disabled");
            }
        }
    });
    $("#agent").trigger("change");


    $("#apiFlag1").change(function() {
        var $this = $(this);
        var checked = $this.is(":checked");
        if (checked) {
            $("#apiUiFlag1").removeAttr("readonly");
            //$("#apiKey").removeAttr("disabled");
            $("#apiIpList").removeAttr("readonly");
        } else {
            $("#apiUiFlag1").attr("readonly", "readonly");
            //$("#apiKey").attr("disabled", "disabled");
            $("#apiIpList").attr("readonly", "readonly");
        }
    });
    $("#apiFlag1").trigger("change");


    $("#smsFlag1").change(function() {
        var $this = $(this);
        var checked = $this.is(":checked");
        if (checked) {
            $("#smsId").removeAttr("readonly");
            $("#smsUsername").removeAttr("readonly");
            $("#smsPassword").removeAttr("readonly");
            $("#smsContent").removeAttr("readonly");
        } else {
            $("#smsId").attr("readonly", "readonly");
            $("#smsUsername").attr("readonly", "readonly");
            $("#smsPassword").attr("readonly", "readonly");
            $("#smsContent").attr("readonly", "readonly");
        }
    });
    $("#smsFlag1").trigger("change");
});

