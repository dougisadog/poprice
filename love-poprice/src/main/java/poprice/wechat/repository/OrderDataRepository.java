package poprice.wechat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.shop.OrderData;

public interface OrderDataRepository extends JpaRepository<OrderData,Long>, JpaSpecificationExecutor<OrderData> {
	OrderData findByOrderNo(String orderNo);
}
