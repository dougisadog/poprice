package me.hao0.wechat.core;

import com.fasterxml.jackson.databind.JavaType;
import me.hao0.wechat.model.template.Tempate;
import me.hao0.common.json.Jsons;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static me.hao0.common.util.Preconditions.*;

/**
 * 菜单组件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class Tempates extends Component {

    /**
     * 查询已添加模板
     */
    private static final String GET = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=";

    private static final JavaType ARRAY_LIST_TEMPLATE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Tempate.class);

    Tempates(){}

    /**
     * 查询模板
     * @return 菜单列表
     */
    public List<Tempate> get(){
        return get(loadAccessToken());
    }

    /**
     * 查询模板
     * @param cb 回调
     */
    public void get(Callback<List<Tempate>> cb){
        get(loadAccessToken(), cb);
    }

    /**
     * 查询模板
     * @param accessToken accessToken
     * @param cb 回调
     */
    public void get(final String accessToken, Callback<List<Tempate>> cb){
        doAsync(new AsyncFunction<List<Tempate>>(cb) {
            @Override
            public List<Tempate> execute() {
                return get(accessToken);
            }
        });
    }

    /**
     * 查询模板
     * @param accessToken accessToken
     * @return 模板列表
     */
    public List<Tempate> get(String accessToken){
        checkNotNullAndEmpty(accessToken, "accessToken");

        String url = GET + accessToken;
        Map<String, Object> resp =  doGet(url);
        String jsonTempate = Jsons.DEFAULT.toJson(resp.get("template_list"));
        return Jsons.EXCLUDE_DEFAULT.fromJson(jsonTempate, ARRAY_LIST_TEMPLATE);
    }

}
