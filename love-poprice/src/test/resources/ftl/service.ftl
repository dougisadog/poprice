${packageName};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

import ${classPackageName};
import ${repositoryClassPackageName};

@Service
@Transactional
public class ${className}Service {
    private final Logger log = LoggerFactory.getLogger(${className}Service.class);

    @Inject
    private ${repositoryClassName} ${repositoryClassSimpleName};

}
