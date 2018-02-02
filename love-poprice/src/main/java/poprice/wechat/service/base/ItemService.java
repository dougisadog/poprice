package poprice.wechat.service.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

import poprice.wechat.repository.base.ItemRepository;

@Service
@Transactional
public class ItemService {
    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    @Inject
    private ItemRepository menuRepository;

}
