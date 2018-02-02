package poprice.wechat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.shop.Address;

public interface AddressRepository extends JpaRepository<Address,Long>, JpaSpecificationExecutor<Address> {

}
