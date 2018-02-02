package poprice.wechat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poprice.wechat.domain.Role;
import poprice.wechat.domain.User;
import poprice.wechat.repository.RoleRepository;
import poprice.wechat.repository.UserRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional
public class RoleService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    public void save(Role role) {
        roleRepository.save(role);
    }

    public void delete(Long id) {
        Role role = roleRepository.findOne(id);
        //先删除user关联
        List<User> users = userRepository.findAllByRoleId(id);
        for (User u : users) {
            u.getRoles().remove(role);
        }
        roleRepository.delete(id);
    }


}
