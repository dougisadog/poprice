!function ($) {
    //初始化省份菜单
    var prov = new Array();
    prov[0] = "北京";
    prov[1] = "天津";
    prov[2] = "上海";
    prov[3] = "重庆";
    prov[4] = "河北";
    prov[5] = "山西";
    prov[6] = "辽宁";
    prov[7] = "吉林";
    prov[8] = "黑龙江";
    prov[9] = "江苏";
    prov[10] = "浙江";
    prov[11] = "安徽";
    prov[12] = "福建";
    prov[13] = "江西";
    prov[14] = "山东";
    prov[15] = "河南";
    prov[16] = "湖北";
    prov[17] = "湖南";
    prov[18] = "广东";
    prov[19] = "甘肃";
    prov[20] = "四川";
    prov[21] = "贵州";
    prov[22] = "海南";
    prov[23] = "云南";
    prov[24] = "青海";
    prov[25] = "陕西";
    prov[26] = "广西";
    prov[27] = "西藏";
    prov[28] = "宁夏";
    prov[29] = "新疆";
    prov[30] = "内蒙古";

    var city = new Array();
    city[0] = new Array("北京");
    city[1] = new Array("天津");
    city[2] = new Array("上海");
    city[3] = new Array("重庆","涪陵","黔江","万州");
    city[4] = new Array("石家庄","衡水","邢台","邯郸","沧州","唐山","廊坊","秦皇岛","承德","保定","张家口");
    city[5] = new Array("太原","晋中","吕梁","忻州","朔州","大同","临汾","运城","阳泉","长治","晋城");
    city[6] = new Array("沈阳","辽阳","铁岭","抚顺","鞍山","营口","大连","本溪","丹东","锦州","朝阳","阜新","盘锦","葫芦岛");
    city[7] = new Array("长春","吉林","延吉","延边","珲春","通化","白山","梅河口","四平","辽源","白城","松原");
    city[8] = new Array("哈尔滨","绥化","伊春","佳木斯","鹤岗","七台河","双鸭山","牡丹江","鸡西","齐齐哈尔","大庆","黑河","大兴安岭");
    city[9] = new Array("南京","镇江","常州","无锡","苏州","徐州","连云港","淮安","宿迁","盐城","扬州","泰州","南通");
    city[10] = new Array("杭州","绍兴","湖州","嘉兴","宁波","舟山","台州","金华","丽水","衢州","温州");
    city[11] = new Array("合肥","淮南","蚌埠","宿州","淮北","阜阳","亳州","六安","巢湖","滁州","芜湖","宣城","马鞍山","铜陵","黄山","安庆","池州");
    city[12] = new Array("福州","莆田","宁德","南平","厦门","泉州","漳州","龙岩","三明");
    city[13] = new Array("南昌","九江","景德镇","上饶","鹰潭","宜春","萍乡","新余","赣州","吉安","抚州");
    city[14] = new Array("济南","聊城","德州","淄博","滨州","东营","潍坊","烟台","威海","青岛","泰安","莱芜","济宁","菏泽","临沂","日照","枣庄");
    city[15] = new Array("郑州","新乡","焦作","济源","安阳","濮阳","鹤壁","许昌","漯河","驻马店","信阳","潢川","周口","平顶山","洛阳","三门峡","南阳","开封","商丘");
    city[16] = new Array("江汉","武汉","孝感","仙桃","荆州","黄石","鄂州","咸宁","黄冈","襄阳","随州","十堰","宜昌","恩施","荆门");
    city[17] = new Array("长沙","湘潭","株洲","益阳","岳阳","常德","湘西","吉首","娄底","怀化","衡阳","邵阳","郴州","永州","张家界");
    city[18] = new Array("广州","清远","东莞","梅州","汕头","惠州","汕尾","河源","深圳","珠海","潮州","韶关","揭阳","湛江","茂名","肇庆","云浮","佛山","中山","江门","阳江");
    city[19] = new Array("兰州","白银","临夏","武威","张掖","酒泉","嘉峪关","金昌","天水","定西","平凉","西峰","庆阳","甘南","陇南");
    city[20] = new Array("成都","乐山","西昌","凉山","攀枝花","德阳","眉山","绵阳","阿坝","雅安","甘孜","广元","遂宁","达州","巴中","南充","广安","内江","资阳","自贡","宜宾","泸州");
    city[21] = new Array("贵阳","毕节","六盘水","铜仁","黔东南","凯里","都匀","黔南","安顺","兴义","黔西南","遵义");
    city[22] = new Array("海口","三亚");
    city[23] = new Array("思茅","昆明","玉溪","曲靖","昭通","红河","文山","普洱","西双版纳","大理","怒江","丽江","迪庆","楚雄","临沧","保山","德宏");
    city[24] = new Array("海晏","共和","德令哈","西宁","海东","黄南","海北","海南","果洛","玉树","格尔木","海西");
    city[25] = new Array("西安","咸阳","渭南","延安","榆林","宝鸡","汉中","安康","商洛","商州","铜川");
    city[26] = new Array("南宁","崇左","百色","钦州","北海","玉林","贵港","防城港","桂林","贺州","梧州","柳州","来宾","河池");
    city[27] = new Array("拉萨","那曲","昌都","山南","日喀则","阿里","林芝");
    city[28] = new Array("银川","吴忠","石嘴山","中卫","固原");
    city[29] = new Array("乌鲁木齐","昌吉","石河子","奎屯","博乐","博尔塔拉","克拉玛依","塔城","伊犁","阿勒泰","吐鲁番","哈密","库尔勒","巴音郭楞","阿克苏","喀什","克州","和田");
    city[30] = new Array("呼和浩特","乌兰察布","集宁","包头","临河","巴彦淖尔","乌海","东胜","鄂尔多斯","海拉尔","呼伦贝尔","赤峰","锡林郭勒盟","锡林浩特","通辽","兴安盟","乌兰浩特","阿拉善盟");
    jQuery.extend({
        provinceCity: function (selectorProvince, selectorCity, hasCountry, autoTriggerIfOneCity, hasCountryWithBlank) {
            var provinceValue = selectorProvince.data('value');
            selectorProvince.empty();
            if (hasCountry) {
                if (hasCountryWithBlank) {
                    selectorProvince.append('<option value="">请选择省份</option>');
                }
                if (provinceValue == "全国") {
                    selectorProvince.append('<option value="全国" selected="selected">全国</option>');
                } else {
                    selectorProvince.append('<option value="全国">全国</option>');
                }

            } else {
                selectorProvince.append('<option value="">请选择省份</option>');
            }

            for (var i = 0; i < prov.length; i++) {
                if (provinceValue == prov[i]) {
                    selectorProvince.append('<option value="' + prov[i] + '" selected="selected">' + prov[i] + '</option>');
                } else {
                    selectorProvince.append('<option value="' + prov[i] + '">' + prov[i] + '</option>');
                }
            }

            var cityValue = selectorCity.data("value");
            selectorProvince.on("change", function () {
                selectorCity.empty();
                selectorCity.append("<option value=''>请选择城市</option>");
                var n = hasCountry ? 1 : 0;
                var indexProv = selectorProvince.find("option:selected").index() - n;//取得选中的想的数组下标0
                if (indexProv < 1) {
                    return;
                }
                indexProv--;
                for (var i = 0; i < city[indexProv].length; i++) {
                    var cityValue1 = city[indexProv][i];
                    if (cityValue == cityValue1 || city[indexProv].length == 1 && autoTriggerIfOneCity) {
                        selectorCity.append('<option value="' + cityValue1 + '" selected="selected">' + cityValue1 + '</option>');
                    } else {
                        selectorCity.append('<option value="' + cityValue1 + '">' + cityValue1 + '</option>');
                    }
                }
            });
            selectorProvince.trigger('change');
            /*
            $("#selectCity").on("change", function () {
                //console.log( $("#selectProv>option:selected").val() + $("#selectCity>option:selected").val() );
            });//*/
        }
    });




}(window.jQuery);