package poprice.wechat.web.mvc.account;

import com.google.common.collect.Lists;

import poprice.wechat.Constants;
import poprice.wechat.domain.AuditLog;
import poprice.wechat.repository.AuditLogRepository;
import poprice.wechat.util.Servlets;
import poprice.wechat.util.persistence.SearchFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 角色的CRUD，字典表, 无分页和搜索，可以页面排序。
 */
@Controller
@RequestMapping("/account/audit-log")
public class AuditLogController {
    private final Logger log = LoggerFactory.getLogger(AuditLogController.class);

    @Inject
    private AuditLogRepository auditLogRepository;

    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
                       @RequestParam(value = "sortProp", defaultValue = "id") String sortProp,
                       @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
                       final Model model, final HttpServletRequest request) {
        if (pageSize == 0) {
            pageSize = Constants.getInt("page_size");
        }
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Specification<AuditLog> spec = SearchFilter.buildSpecificationByParams(searchParams, AuditLog.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Page<AuditLog> page = auditLogRepository.findAll(spec, pageRequest);
        model.addAttribute("page", page);

        return "account/audit-log/list";
    }

}

