package poprice.wechat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.shop.Goods;
import poprice.wechat.domain.shop.OrderData;

public interface GoodsRepository extends JpaRepository<Goods,Long>, JpaSpecificationExecutor<Goods> {

}
