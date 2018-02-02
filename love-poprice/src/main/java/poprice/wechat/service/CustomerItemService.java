package poprice.wechat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

import poprice.wechat.repository.CustomerItemRepository;

@Service
@Transactional
public class CustomerItemService {
    private final Logger log = LoggerFactory.getLogger(CustomerItemService.class);

    @Inject
    private CustomerItemRepository memberMenuRepository;

}
