package poprice.wechat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import poprice.wechat.Constants;
import poprice.wechat.domain.Customer;
import poprice.wechat.domain.CustomerItem;
import poprice.wechat.domain.CustomerItemRecord;
import poprice.wechat.domain.base.Item;
import poprice.wechat.repository.CustomerItemRecordRepository;
import poprice.wechat.repository.CustomerItemRepository;
import poprice.wechat.repository.base.ItemRepository;
import poprice.wechat.security.SecurityUtils;

@Service
@Transactional
public class CustomerItemRecordService {
    private final Logger log = LoggerFactory.getLogger(CustomerItemRecordService.class);

    @Inject
    private CustomerItemRecordRepository customerItemRecordRepository;

    @Inject
    private CustomerItemRepository customerItemRepository;

    @Inject
    private ItemRepository itemRepository;

    public void save(Customer entity, Long[] buyIds, Integer[] buyNumbers, Long[] giveIds, Integer[] giveNumbers, String remark) {
        String record = "购买";

        CustomerItemRecord customerItemRecord = new CustomerItemRecord();
        CustomerItem customerItem;

        for (int i = 0; i < buyNumbers.length; i++) {
            Integer buyNumber = buyNumbers[i];
            if (buyNumber != null) {
                Long buyId = buyIds[i];
                Item item = itemRepository.findOne(buyId);
                record += "[" + item.getTitle() + "]" + "[" + buyNumber + "]次    ";

                for (int j = 0; j < buyNumber; j++) {
                    customerItem = new CustomerItem();
                    customerItem.setCustomer(entity);
                    customerItem.setItemType(Constants.ItemType.b);
                    customerItem.setActionType(Constants.ActionType.rec);
                    customerItem.setItem(item);
                    customerItemRepository.save(customerItem);
                }

            }
        }

        record += "赠送";
        for (int i = 0; i < giveNumbers.length; i++) {
            Integer giveNumber = giveNumbers[i];
            if (giveNumber != null) {
                Long giveId = giveIds[i];
                Item item = itemRepository.findOne(giveId);
                record += "[" + item.getTitle() + "]" + "[" + giveNumber + "]次    ";

                for (int j = 0; j < giveNumber; j++) {
                    customerItem = new CustomerItem();
                    customerItem.setCustomer(entity);
                    customerItem.setItemType(Constants.ItemType.g);
                    customerItem.setActionType(Constants.ActionType.rec);
                    customerItem.setItem(item);
                    customerItemRepository.save(customerItem);
                }

            }
        }

        customerItemRecord.setCustomer(entity);
        customerItemRecord.setActionType(Constants.ActionType.rec);
        customerItemRecord.setRecord(record);
        customerItemRecord.setRemark(remark);
        customerItemRecordRepository.save(customerItemRecord);
    }

    public void consume(Customer entity, Item item,Constants.ItemType type){
        String record = "消费";

        CustomerItemRecord customerItemRecord = new CustomerItemRecord();
        CustomerItem customerItem;

        record += "[" + item.getTitle() + "]" + "[" + 1 + "]次    ";

        customerItem = new CustomerItem();
        customerItem.setCustomer(entity);
        customerItem.setItemType(type);
        customerItem.setActionType(Constants.ActionType.consumer);
        customerItem.setItem(item);
        customerItemRepository.save(customerItem);

        customerItemRecord.setCustomer(entity);
        customerItemRecord.setActionType(Constants.ActionType.consumer);
        customerItemRecord.setRecord(record);
        customerItemRecord.setRemark("");
        customerItemRecordRepository.save(customerItemRecord);
    }

}
