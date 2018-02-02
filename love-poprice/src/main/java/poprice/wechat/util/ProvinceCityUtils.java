package poprice.wechat.util;

import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ProvinceCityUtils {
    private static Map<String, List<String>> map = new LinkedHashMap<>();
    static  {
        List<String> list = null;
        map.put("北京", Lists.newArrayList("北京"));
        map.put("天津", Lists.newArrayList("天津"));
        map.put("上海", Lists.newArrayList("上海"));
        map.put("重庆", Lists.newArrayList("重庆","涪陵","黔江","万州"));
        map.put("河北", Lists.newArrayList("石家庄","衡水","邢台","邯郸","沧州","唐山","廊坊","秦皇岛","承德","保定","张家口"));
        map.put("山西", Lists.newArrayList("太原","晋中","吕梁","忻州","朔州","大同","临汾","运城","阳泉","长治","晋城"));
        map.put("辽宁", Lists.newArrayList("沈阳","辽阳","铁岭","抚顺","鞍山","营口","大连","本溪","丹东","锦州","朝阳","阜新","盘锦","葫芦岛"));
        map.put("吉林", Lists.newArrayList("长春","吉林","延吉","延边","珲春","通化","白山","梅河口","四平","辽源","白城","松原"));
        map.put("黑龙江", Lists.newArrayList("哈尔滨","绥化","伊春","佳木斯","鹤岗","七台河","双鸭山","牡丹江","鸡西","齐齐哈尔","大庆","黑河","大兴安岭"));
        map.put("江苏", Lists.newArrayList("南京","镇江","常州","无锡","苏州","徐州","连云港","淮安","宿迁","盐城","扬州","泰州","南通"));
        map.put("浙江", Lists.newArrayList("杭州","绍兴","湖州","嘉兴","宁波","舟山","台州","金华","丽水","衢州","温州"));
        map.put("安徽", Lists.newArrayList("合肥","淮南","蚌埠","宿州","淮北","阜阳","亳州","六安","巢湖","滁州","芜湖","宣城","马鞍山","铜陵","黄山","安庆","池州"));
        map.put("福建", Lists.newArrayList("福州","莆田","宁德","南平","厦门","泉州","漳州","龙岩","三明"));
        map.put("江西", Lists.newArrayList("南昌","九江","景德镇","上饶","鹰潭","宜春","萍乡","新余","赣州","吉安","抚州"));
        map.put("山东", Lists.newArrayList("济南","聊城","德州","淄博","滨州","东营","潍坊","烟台","威海","青岛","泰安","莱芜","济宁","菏泽","临沂","日照","枣庄"));
        map.put("河南", Lists.newArrayList("郑州","新乡","焦作","济源","安阳","濮阳","鹤壁","许昌","漯河","驻马店","信阳","潢川","周口","平顶山","洛阳","三门峡","南阳","开封","商丘"));
        map.put("湖北", Lists.newArrayList("江汉","武汉","孝感","仙桃","荆州","黄石","鄂州","咸宁","黄冈","襄阳","随州","十堰","宜昌","恩施","荆门"));
        map.put("湖南", Lists.newArrayList("长沙","湘潭","株洲","益阳","岳阳","常德","湘西","吉首","娄底","怀化","衡阳","邵阳","郴州","永州","张家界"));
        map.put("广东", Lists.newArrayList("广州","清远","东莞","梅州","汕头","惠州","汕尾","河源","深圳","珠海","潮州","韶关","揭阳","湛江","茂名","肇庆","云浮","佛山","中山","江门","阳江"));
        map.put("甘肃", Lists.newArrayList("兰州","白银","临夏","武威","张掖","酒泉","嘉峪关","金昌","天水","定西","平凉","西峰","庆阳","甘南","陇南"));
        map.put("四川", Lists.newArrayList("成都","乐山","西昌","凉山","攀枝花","德阳","眉山","绵阳","阿坝","雅安","甘孜","广元","遂宁","达州","巴中","南充","广安","内江","资阳","自贡","宜宾","泸州"));
        map.put("贵州", Lists.newArrayList("贵阳","毕节","六盘水","铜仁","黔东南","凯里","都匀","黔南","安顺","兴义","黔西南","遵义"));
        map.put("海南", Lists.newArrayList("海口","三亚"));
        map.put("云南", Lists.newArrayList("思茅","昆明","玉溪","曲靖","昭通","红河","文山","普洱","西双版纳","大理","怒江","丽江","迪庆","楚雄","临沧","保山","德宏"));
        map.put("青海", Lists.newArrayList("海晏","共和","德令哈","西宁","海东","黄南","海北","海南","果洛","玉树","格尔木","海西"));
        map.put("陕西", Lists.newArrayList("西安","咸阳","渭南","延安","榆林","宝鸡","汉中","安康","商洛","商州","铜川"));
        map.put("广西", Lists.newArrayList("南宁","崇左","百色","钦州","北海","玉林","贵港","防城港","桂林","贺州","梧州","柳州","来宾","河池"));
        map.put("西藏", Lists.newArrayList("拉萨","那曲","昌都","山南","日喀则","阿里","林芝"));
        map.put("宁夏", Lists.newArrayList("银川","吴忠","石嘴山","中卫","固原"));
        map.put("新疆", Lists.newArrayList("乌鲁木齐","昌吉","石河子","奎屯","博乐","博尔塔拉","克拉玛依","塔城","伊犁","阿勒泰","吐鲁番","哈密","库尔勒","巴音郭楞","阿克苏","喀什","克州","和田"));
        map.put("内蒙古", Lists.newArrayList("呼和浩特","乌兰察布","集宁","包头","临河","巴彦淖尔","乌海","东胜","鄂尔多斯","海拉尔","呼伦贝尔","赤峰","锡林郭勒盟","锡林浩特","通辽","兴安盟","乌兰浩特","阿拉善盟"));
    }

    public static Set<String> getProvinceSet() {
        return map.keySet();
    }

    public static Map<String, List<String>> getAll() {
        return map;
    }

}
