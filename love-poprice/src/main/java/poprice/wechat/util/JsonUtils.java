package poprice.wechat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * http://wiki.fasterxml.com/JacksonInFiveMinutes
 */
public class JsonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJsonString(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            return null;
        }
    }
}
