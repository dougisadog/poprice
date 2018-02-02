package poprice.wechat.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.qq.wechat.WechatHaoHelper;

import me.hao0.wechat.model.user.User;
import me.hao0.wechat.model.user.UserList;
import poprice.wechat.domain.Customer;
import poprice.wechat.service.CustomerService;
import poprice.wechat.util.Collections3;

/**
 * Created by zhangyz on 17-3-28.
 */
@Component
public class WechatUserInfoSync {
    private static Logger logger = LoggerFactory.getLogger(WechatUserInfoSync.class);
    
    private static final String MSG = "执行微信用户及其头像更新";
    
    @Inject
    private CustomerService customerService;
    
    @Scheduled(fixedRate = 6 * 60 * 1000)
    public void updateUserInfo() {
        logger.info(MSG + "开始");
        try {
        	updateCustomer();
        } catch (Exception e) {
            logger.error(MSG + "异常", e);

        }
        logger.info(MSG + "结束");
    }
    
    private boolean isNeedUpdate(Customer c, User user) {
    	return c.getOpenId().equals(user.getOpenId()) && !c.getHeadImgUrl().equals(user.getHeadImgUrl());
    	
    }
    
    private void updateCustomer() {
        // 获取当前openid集合
        List<Customer> customers = customerService.findAll();
        List<String> openIds = customers.stream().map(Customer::getOpenId)
                .collect(Collectors.toList());
        // 微信服务上拉取最新用户openid集合
        List<String> currentOpenIds = WechatHaoHelper.getInstance().getUsers().getData().getOpenId();
        
        // 获取新增openid
//        List<String> newOpenIds = Collections3.subtract(currentOpenIds, openIds);

        List<Customer> newCustomers = new ArrayList<>();
        for (String openId : currentOpenIds) {
			User user = WechatHaoHelper.getInstance().getUserDetail(openId);
            Customer currentCustomer = new Customer(user);
            //旧用户
            if (openIds.contains(openId)) {
            	//判断头像更新
				List<Customer> oldCustomers = customers.stream()
						.filter(c -> isNeedUpdate(c, user)).collect(Collectors.toList());
				int size = oldCustomers.size();
				//更新头像
				if (size > 0) {
					currentCustomer.setId(oldCustomers.remove(0).getId());
					newCustomers.add(currentCustomer);
				}
				//多余相同openId去重删除
				if (size > 1) {
					customerService.delete(oldCustomers);
				}
            }
            //新用户
            else {
            	newCustomers.add(currentCustomer);
            }
        }
        customerService.save(newCustomers);
    }
}
