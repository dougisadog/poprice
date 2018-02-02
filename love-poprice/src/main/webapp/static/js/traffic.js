$(document).ready(function(){
    highlight();
    initDeleteTip();

    if ($("#searchForm").size() > 0) {
        if ($("#searchForm").find(".focus-onload:visible").size() > 0) {
            $("#searchForm").find(".focus-onload:visible").first().select();
        } else {
            $("#searchForm").find("input:visible").first().select();
        }
    } else if ($("#inputForm").size() > 0) {
        if ($("#inputForm").find(".focus-onload:visible").size() > 0) {
            $("#inputForm").find(".focus-onload:visible").first().select();
        } else {
            $("#inputForm").find("input:visible").first().select();
        }
    }

    function initDeleteTip() {
        $(".delete").click(function(e) {
            if (confirm("确认要删除这条数据吗？")) {
                return true;
            }
            e.preventDefault();
            e.stopPropagation();
            return false;
        });
    }

    //只输入数字的
    $("input.digits-only").bind("keypress", function (event) {
        if (event.keyCode <48 || event.keyCode > 57) {
            return false;
        }
        return true;
    });

    //keep message, do not remove it.
    //setTimeout("__removeTipMessages()",10000);
});

/**
 * 寻找最大正向匹配
 */
function highlight() {
    var contextPath = getCurrentContextPath();

    //menus highlight
    var highlighted = false;
    var host = document.location.host;
    var _locatonHref = document.location.href;
    var _pos = _locatonHref.indexOf(host) + host.length + contextPath.length;
    _locatonHref = _locatonHref.substring(_pos); //取得开始位置

    var _qmPos = 0;
    if ((_qmPos = _locatonHref.indexOf("?")) > 0) {
        //if ? exists, need exclude '/' in parameters, 问号后面都是参数,不要了.
        _locatonHref = _locatonHref.substr(0, _qmPos);
    }
    var _hrefForDataMenu = _locatonHref; //保留一下去掉问号的, 用于需要特定处理的
    var _lastPos = _locatonHref.lastIndexOf('/');
    _locatonHref = _locatonHref.substring(0, _lastPos);

    if (_locatonHref.split('/').length > 3) {
        _locatonHref = _locatonHref.substring(0, _locatonHref.lastIndexOf('/'));
    }

    //alert(_locatonHref + ":" + _hrefForDataMenu);
    //th:attr="data-menu=@{/user/role,/user/}" data first(with class menu), href auto but second.
    $("ul.nav li.menu a").each(function(e) {
        var _a = $(this);
        var href1 = _a.data("menu");
        if (href1 == null) return true;
        var array = href1.split(',');

        for (var i = 0; i < array.length; i++) {
            var href = $.trim(array[i]);
            if (href.indexOf(contextPath) == 0) {
                href = $.trim(href.substring(contextPath.length));
            }
            if (href == '' || href == '/') {
                continue;
            }
            //console.log(_a.text() + ":" + array[i] + ":" + href);
            if (_hrefForDataMenu.indexOf(href) === 0) {
                //alert("highlight:" + _hrefForDataMenu + ":" + href);
                _a.parent('li').addClass('active');
                highlighted = true;
                return false;
            }
        }
    });

    if (!highlighted) {
        $("ul.nav li[class!='menu'] a").each(function(e) {
            var _a = $(this);
            var href = _a.attr('href').substring(contextPath.length);
            //光开头不行,还得是有/结尾或者全等,否则/agent和/agent-member分不清

            if (href.indexOf(_locatonHref) === 0 && (href === _locatonHref || href.indexOf(_locatonHref + '/') === 0)) {
                _a.parent('li').addClass('active');
                highlighted = true;
                return false;
            }
        });
    }

    if (!highlighted) {
        //如果还没找到,那说明可能_locatonHref裁剪的不够,再尝试一次
        _locatonHref = _locatonHref.substring(0, _locatonHref.lastIndexOf('/'));
        $("ul.nav li[class!='menu'] a").each(function(e) {
            var _a = $(this);
            var href = _a.attr('href').substring(contextPath.length);
            if (href.indexOf(_locatonHref) === 0 && (href === _locatonHref || href.indexOf(_locatonHref + '/') === 0)) {
                _a.parent('li').addClass('active');
                highlighted = true;
                return false;
            }
        });
    }
}

function __removeTipMessages() {
    var blocks = $("div.alert.alert-block.fade.in");
    blocks.fadeOut("slow", function() {
        $(this).remove();
    });
}

function selectAllCheckbox(selector, event) {
    //event.preventDefault();
    $(selector).prop("checked", true);return false;
}
function cancelAllCheckbox(selector, event) {
    //event.preventDefault();
    $(selector).prop("checked", false);return false;
}

function getCurrentContextPath() {
    var scripts = document.getElementsByTagName("script");
    var src = scripts[0].src;
    var host = document.location.host;
    var pos1 = src.indexOf(host) + host.length;
    var pos2 = src.indexOf("/static/");
    var contextPath = src.substring(pos1, pos2);
    return contextPath;
}

function showLabelMessage(selector, message) {
    selector.text(message);
    selector.removeClass("error");
    selector.addClass("success");
}
function showLabelError(selector, message) {
    selector.text(message);
    selector.removeClass("success");
    selector.addClass("error");
}

!function ($) {
    $.fn.heightFix = function() {
        var elements = $(this);
        var maxHeight = -1;
        elements.each(function() {
            var el = $(this);
            if (el.height() > maxHeight) maxHeight = el.height();
        });
        if (maxHeight > 0) {
            elements.height(maxHeight);
        }
    };

    $.fn.minHeightFix = function() {
        var elements = $(this);
        var maxHeight = -1;
        elements.each(function() {
            var el = $(this);
            if (el.height() > maxHeight) maxHeight = el.height();
        });
        if (maxHeight > 0) {
            elements.css('min-height', maxHeight);
        }
    };
}(window.jQuery);