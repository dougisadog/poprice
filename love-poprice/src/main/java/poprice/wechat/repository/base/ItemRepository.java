package poprice.wechat.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import poprice.wechat.domain.base.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long>, JpaSpecificationExecutor<Item> {
    /**
     * 按名字排序
     * @return
     */
    @Query(value = "select * from item i  order by CONVERT(i.title USING GBK)", nativeQuery = true)
    List<Item> findAll();

}
