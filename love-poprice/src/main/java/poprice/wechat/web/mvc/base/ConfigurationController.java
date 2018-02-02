package poprice.wechat.web.mvc.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import poprice.wechat.Constants;
import poprice.wechat.domain.base.Configuration;
import poprice.wechat.repository.base.ConfigurationRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuditLogService;
import poprice.wechat.service.ConfigurationService;
import poprice.wechat.util.RestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 系统配置的CRUD，字典表, 无分页和搜索，可以页面排序。
 */
@Controller
@RequestMapping("/base/configuration")
public class ConfigurationController {
    private final Logger log = LoggerFactory.getLogger(ConfigurationController.class);

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private AuditLogService auditLogService;


    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model) {
        Configuration entity = configurationRepository.findOne(id);
        model.addAttribute("entity", entity);

        //parse json key & value
        String jsonValue = entity.getValue();

        try {
            Map<String, Object> map = mapper.readValue(jsonValue, LinkedHashMap.class);
            model.addAttribute("map", map);
            //KEY的汉字见i18n文件
        } catch (IOException e) {
            log.error("配置json读取解析错误", e);
        }

        return "base/configuration/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id") final Long id , final HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        Configuration exist = configurationRepository.findOne(id);
        if (exist == null) {
            return null;
        }
        Map<String, String> map = Maps.newLinkedHashMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (name.equals("id")) {
                continue;
            }
            String parameterValue = request.getParameter(name);
            if (Strings.isNullOrEmpty(parameterValue)) {
                redirectAttributes.addFlashAttribute("flash.error", "更新配置'" + exist.getTitle() + "'失败，配置值不可以为空");
                return "redirect:/base/configuration/edit/" + id;
            }
            map.put(name, parameterValue.trim());
        }

        try {
            String value = mapper.writeValueAsString(map);
            exist.setValue(value);
            configurationRepository.save(exist);
            configurationService.organizeData();
        } catch (JsonProcessingException e) {
            log.error("配置json写入解析错误", e);
        }

        redirectAttributes.addFlashAttribute("flash.message", "更新配置'" + exist.getTitle() + "'成功");
        log.info("{}更新系统配置{}成功, VALUE:{}", SecurityUtils.getCurrentLogin(), exist.getName(), exist.getValue());

        auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.conf, Constants.ActionType.u, "修改成功");

        return "redirect:/base/configuration/edit/" + id;
    }


}

