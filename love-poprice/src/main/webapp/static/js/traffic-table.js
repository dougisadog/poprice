
(function ($) {

    $.fn.searchTable = function (options) {

        //创建一些默认值，拓展任何被提供的选项
        var settings = $.extend({
            'searchFormId': '#searchForm',
            'searchFormAction': '',
            'searchFormSortPropId': '#sortProp',
            'searchFormSortOrderId': '#sortOrder',
            'searchFormPageId': '#page',
            'searchFormPageSizeId': '#pageSize',
            'paginationWrapperClass': 'pagination-wrapper',
            'headerIgnoreClass': 'sorter-false'
        }, options);

        var searchForm = $(settings.searchFormId);
        if (settings.searchFormAction == '') {
            settings.searchFormAction = searchForm.attr("action");
        }

        var sortProp = $(settings.searchFormSortPropId).val();
        var sortOrder = $(settings.searchFormSortOrderId).val();

        var headers = this.find("th");
        headers.each(function() {
            var header = $(this);
            if (header.hasClass(settings.headerIgnoreClass)) {
                return;
            }
            var text = header.html();
            if (header.hasClass("tablesorter-header")) {
                if (header.data('prop') == sortProp) {
                    if (sortOrder.toLowerCase() == 'asc') {
                        header.html('<div class="tablesorter-wrapper"><div class="tablesorter-header-inner">' +text+' <i class="tablesorter-icon glyphicon glyphicon-chevron-up"></i></div></div>');
                    } else {
                        header.html('<div class="tablesorter-wrapper"><div class="tablesorter-header-inner">' +text+' <i class="tablesorter-icon glyphicon glyphicon-chevron-down"></i></div></div>');
                    }
                } else {
                    header.html('<div class="tablesorter-wrapper"><div class="tablesorter-header-inner">' +text+' <i class="tablesorter-icon bootstrap-icon-unsorted"></i></div></div>');
                }

                header.click(function() {
                    var clicked = $(this);
                    var prop = clicked.data('prop');
                    if (prop == sortProp) {
                        $(settings.searchFormSortOrderId).val(sortOrder == 'asc'?'desc':'asc');
                    } else {
                        $(settings.searchFormSortPropId).val(prop);
                        $(settings.searchFormSortOrderId).val('asc');
                    }
                    searchForm.submit();
                });
            } else {
                header.html('<div class="tablesorter-wrapper"><div class="tablesorter-header-inner">' +text+'</div></div>');
            }
        });

        if (settings.paginationWrapperClass != '') {
            $("." + settings.paginationWrapperClass + " a").click(function(e) {

                e.preventDefault();
                var page = $(this).data('page');
                if (page == null || page == '') {
                    return false;
                }
                $(settings.searchFormPageId).val(page);
                searchForm.submit();
            });

            $("." + settings.paginationWrapperClass + " button").click(function(e) {
                e.preventDefault();
                var page = $("input.current-val").val();
                if (page == null || page == '' || page.match(/[^0-9]/g)) {
                    page =$("#page").val();
                    $("input.current-val").val(page);
                }
                $(settings.searchFormPageId).val(page);
                searchForm.submit();
            });

        }

        searchForm.find("button[type=submit]").click(function() {
            $(settings.searchFormPageId).val("1");
        });
    };


})(jQuery);

