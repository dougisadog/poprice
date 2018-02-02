package poprice.wechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.Customer;
import poprice.wechat.domain.CustomerItemRecord;

import java.util.List;


public interface CustomerItemRecordRepository extends JpaRepository<CustomerItemRecord,Long>, JpaSpecificationExecutor<CustomerItemRecord> {

    List<CustomerItemRecord> findByCustomerOrderByIdDesc(Customer customer);
}
