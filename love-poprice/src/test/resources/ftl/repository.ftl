${packageName};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ${classPackageName};


public interface ${className}Repository extends JpaRepository<${className},Long>, JpaSpecificationExecutor<${className}> {

}
