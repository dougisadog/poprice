package poprice.wechat.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.qq.wechat.utils.CharUtils;

import poprice.wechat.domain.Customer;
import poprice.wechat.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Inject
    private CustomerRepository customerRepository;
    
    public List<Customer> findAll() {
    	return customerRepository.findAll();
    }
    
    public Customer findByOpenId(String openId) {
    	return customerRepository.findByOpenId(openId);
    }
    
    public void delete (Customer customer) {
    	customerRepository.delete(customer);
    }
    
    public void delete (List<Customer> customers) {
    	customerRepository.delete(customers);
    }
    
    public void delete(Long id) {
    	customerRepository.delete(id);
    }
    
    public void save(Customer customer) {
        String nickName = CharUtils.charValidation(customer.getNickName());
        customer.setNickName(StringUtils.isEmpty(nickName) ? customer.getRealName() : nickName);
    	customerRepository.save(customer);
    }
    
    public void save(List<Customer> customers) {
    	for (Customer customer : customers) {
    		String nickName = CharUtils.charValidation(customer.getNickName());
    		customer.setNickName(StringUtils.isEmpty(nickName) ? customer.getRealName() : nickName);
		}
    	customerRepository.save(customers);
    }

}
