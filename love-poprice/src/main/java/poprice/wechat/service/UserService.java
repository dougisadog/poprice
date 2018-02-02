package poprice.wechat.service;

import com.google.common.base.Strings;

import poprice.wechat.domain.Authority;
import poprice.wechat.domain.Role;
import poprice.wechat.domain.User;
import poprice.wechat.repository.*;
import poprice.wechat.security.SecurityUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * 取消MD5加密，改用sha256高强度加密
     *
     */
    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private MailService mailService;



    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);
        String lowercaseUsername = username.toLowerCase();
        Optional<User> userFromDatabase =  userRepository.findOneByUsername(lowercaseUsername);
        return userFromDatabase.map(user -> {

            if (isSupervisor(user.getId())) { //超级管理员
                for (Authority authority : authorityService.getList()) {
                    user.addToAuthorities(authority);
                }
            } else {
                for (Role role : user.getRoles()) {
                    for (String authName : role.getAuthorities()) {
                        Authority authority = authorityService.getByAuthority(authName);
                        user.addToAuthorities(authority);
                    }
                }
            }

            //log.info("Login user {} authorities:", user.getUsername(), user.getAuthorities().toString());
            log.info("Login user information:" + user.toString());
            return user;
        }).orElseThrow(() -> new UsernameNotFoundException("用户" + lowercaseUsername + "未找到"));
    }


    /**
     * 只有分销商和管理员才可以登录
     *
     * @param username
     * @return
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsernameForLogin(final String username) {
        log.debug("Authenticating {}", username);
        String lowercaseUsername = username.toLowerCase();
        Optional<User> userFromDatabase =  userRepository.findOneByUsername(lowercaseUsername);
        return userFromDatabase.map(user -> {

            if (isSupervisor(user.getId())) { //超级管理员
                for (Authority authority : authorityService.getList()) {
                    user.addToAuthorities(authority);
                }
            } else {
                for (Role role : user.getRoles()) {
                    for (String authName : role.getAuthorities()) {
                        Authority authority = authorityService.getByAuthority(authName);
                        user.addToAuthorities(authority);
                    }
                }
            }

            //log.info("Login user {} authorities:", user.getUsername(), user.getAuthorities().toString());
            log.info("Login user information:" + user.toString());
            return user;
        }).orElseThrow(() -> new UsernameNotFoundException("用户" + lowercaseUsername + "未找到"));
    }

    public void deleteUser(Long id) {
        if (isSupervisor(id)) {
            log.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getCurrentLogin());
            throw new ServiceException("不能删除超级管理员用户");
        }
        //TODO: 这里需要检查一下是否有关联数据，没有任何关联数据才可以删除！

        User entity = userRepository.findOne(id);
        entity.setDeleted(true);
        userRepository.save(entity);

    }

    public void enableUser(Long id) {
        if (isSupervisor(id)) {
            log.warn("操作员{}尝试启用超级管理员用户", SecurityUtils.getCurrentLogin());
            throw new ServiceException("不能操作超级管理员用户");
        }
        User user = userRepository.findOne(id);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void disableUser(Long id) {
        if (isSupervisor(id)) {
            log.warn("操作员{}尝试禁用超级管理员用户", SecurityUtils.getCurrentLogin());
            throw new ServiceException("不能操作超级管理员用户");
        }

        User user = userRepository.findOne(id);
        user.setEnabled(false);
        userRepository.save(user);

    }

    public void resetPassword(Long id) {
        if (isSupervisor(id)) {
            log.warn("操作员{}尝试重置超级管理员用户密码", SecurityUtils.getCurrentLogin());
            throw new ServiceException("不能重置超级管理员用户密码");
        }
        User user = userRepository.findOne(id);
        user.setPassword(passwordEncoder.encode("12345678"));
        userRepository.save(user);
    }

    /**
     * 判断是否超级管理员.
     */
    public boolean isSupervisor(Long id) {
        return id == 1;
    }


    public void saveUser(User user) {
        if (user.getId() == null || !Strings.isNullOrEmpty(user.getPassword1())) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword1());
            user.setPassword(encryptedPassword);
        }
        userRepository.save(user);
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentLogin()).ifPresent(u -> {

            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void changePassword(String password, String newPassword) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(newPassword)) {
                throw new ServiceException("密码不能为空，请重试");
            }

            if (newPassword.length() < 6 || newPassword.length() > 25) {
                throw new ServiceException("密码长度在6-25字符之间");
            }

            if (!Objects.equals(passwordEncoder.encode(password), u.getPassword())) {
                throw new ServiceException("您的原密码输入错误，请重试");
            }

            String encryptedPassword = passwordEncoder.encode(newPassword);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User currentUser = userRepository.findOneByUsername(SecurityUtils.getCurrentLogin()).get();
        currentUser.getAuthorities().size(); // eagerly load the association
        return currentUser;
    }


    @Transactional(readOnly = true)
    public boolean isEmailExist(String email) {
        return userRepository.countByEmail(email) > 0;
    }


}
