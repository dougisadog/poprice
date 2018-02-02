package poprice.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import me.hao0.wechat.model.template.Tempate;
import me.hao0.wechat.model.user.UserList;
import poprice.wechat.Constants;
import poprice.wechat.domain.Customer;
import poprice.wechat.domain.Role;
import poprice.wechat.domain.User;
import poprice.wechat.domain.base.Configuration;
import poprice.wechat.domain.wechat.WechatTemplate;
import poprice.wechat.repository.RoleRepository;
import poprice.wechat.repository.UserRepository;
import poprice.wechat.repository.base.AnnouncementRepository;
import poprice.wechat.repository.base.ConfigurationRepository;
import poprice.wechat.repository.wechat.WechatTemplateRepository;
import poprice.wechat.util.Collections3;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.qq.wechat.WechatHaoHelper;

@Service
public class BootstrapService implements InitializingBean {

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private AnnouncementRepository announcementRepository;

    @Inject
    private WechatTemplateRepository wechatTemplateRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConfiguration();
        initPrivileges();
    }

    private void initConfiguration() {
        Configuration entity = configurationRepository.findOne(1L);
        String value = "{\"page_size\":25,\"company_name\":\"爱上丰婷\",\"company_location\":\"爱上丰婷\",\"attach_location\":\"d:/data/asftys-pf|~/data/asftys-pf|/home/user/data/asftys-pf\"}\n";
        if (entity == null) {
            entity = new Configuration();
            entity.setId(1L);
            entity.setName("系统配置");
            entity.setTitle("system");
            entity.setValue(value);
            configurationRepository.save(entity);
        } else {
            if (entity.getValue() == null || Strings.isNullOrEmpty(entity.getValue().trim())) {
                entity.setValue(value);
                configurationRepository.save(entity);
            }
        }

        configurationService.organizeData();
        Constants.update("announcement", announcementRepository.findByActiveTrue());
    }


    private void initPrivileges() {
        Role role = roleRepository.findOne(1L);
        if (role == null) {
            role = new Role();
            role.setName("系统管理员");
            role.getAuthorities().add("ROLE_ADMIN");
            roleRepository.save(role);
        }

        User user = userRepository.findOne(1L);
        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setName("系统管理员");
            user.setPassword1("123123");
            user.setEmail("admin@localhost");
            user.setCreatedBy("system");
            user.setAdminFlag(true);
            user.setDeleted(false);
            Set<Role> rolesForUser = Sets.newTreeSet();
            rolesForUser.add(roleRepository.findOne(1L));
            user.setRoles(rolesForUser);
            userService.saveUser(user);
        }
        initWechatTemplate();

    }

    private void initWechatTemplate() {
        wechatTemplateRepository.deleteAll();
    	List<Tempate> templates = WechatHaoHelper.getInstance().getWechat().tempates().get();
    	List<WechatTemplate> wechatTemplates = new ArrayList<>();
    	for (Tempate tempate : templates) {
          WechatTemplate wechatTemplate = new WechatTemplate();
          wechatTemplate.setActive(true);
          String content = tempate.getContent();
          wechatTemplate.setTitle(tempate.getTitle());
          wechatTemplate.setContent(content);
          wechatTemplate.setIndustry(tempate.getPrimary_industry());
          wechatTemplate.setTemplateId(tempate.getTemplate_id());
          wechatTemplate.setLink("http://asftys.poprice.cn");
          wechatTemplates.add(wechatTemplate);
		}
    	wechatTemplateRepository.save(wechatTemplates);
    }

}
