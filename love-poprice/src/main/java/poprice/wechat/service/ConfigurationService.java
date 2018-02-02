package poprice.wechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import poprice.wechat.Constants;
import poprice.wechat.domain.Role;
import poprice.wechat.domain.User;
import poprice.wechat.domain.base.Configuration;
import poprice.wechat.repository.RoleRepository;
import poprice.wechat.repository.UserRepository;
import poprice.wechat.repository.base.ConfigurationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

@Service
public class ConfigurationService {
    private final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    @Inject
    private ConfigurationRepository configurationRepository;


    public synchronized void organizeData() {
        ObjectMapper mapper = new ObjectMapper();
        Configuration entity = configurationRepository.findOne(1L);
        String jsonValue = entity.getValue();

        try {
            Map map = mapper.readValue(jsonValue, LinkedHashMap.class);
            Set<Map.Entry> set = map.entrySet();
            for (Map.Entry entry : set) {
                Constants.update(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            log.error("配置json读取解析错误", e);
        }

    }

}
