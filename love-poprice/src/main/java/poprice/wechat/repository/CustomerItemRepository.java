package poprice.wechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poprice.wechat.domain.Customer;
import poprice.wechat.domain.CustomerItem;
import poprice.wechat.domain.base.Item;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;


public interface CustomerItemRepository extends JpaRepository<CustomerItem, Long>, JpaSpecificationExecutor<CustomerItem> {

    @Query("select count(*) from CustomerItem c where c.itemType='b' and c.actionType = 'rec' and c.customer=:customer and c.item=:item")
    Integer findByitemTypeBAndActionTypeRec(@Param("customer") Customer customer,@Param("item") Item item);

    @Query("select count(*) from CustomerItem c where c.itemType='b' and c.actionType = 'consumer' and c.customer=:customer and c.item=:item")
    Integer findByitemTypeBAndActionTypeConsumer(@Param("customer") Customer customer,@Param("item") Item item);

    @Query("select count(*) from CustomerItem c where c.itemType='g' and c.actionType = 'rec' and c.customer=:customer and c.item=:item")
    Integer findByitemTypeGAndActionTypeRec(@Param("customer") Customer customer,@Param("item") Item item);

    @Query("select count(*) from CustomerItem c where c.itemType='g' and c.actionType = 'consumer' and c.customer=:customer and c.item=:item")
    Integer findByitemTypeGAndActionTypeConsumer(@Param("customer") Customer customer,@Param("item") Item item);

}
