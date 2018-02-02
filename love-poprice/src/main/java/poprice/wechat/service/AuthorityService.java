package poprice.wechat.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import poprice.wechat.domain.Authority;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


@Service
public class AuthorityService {


    private List<Authority> list;

    private Map<String, Authority> map = Maps.newTreeMap();

    public AuthorityService() {
        loadData();
    }

    /**
     * http://howtodoinjava.com/2014/05/04/read-file-line-by-line-in-java-8-streams-of-lines-example/
     * http://www.adam-bien.com/roller/abien/entry/java_8_reading_a_file
     *
     */
    private void loadData() {
        List<Authority> list = Lists.newArrayList();
        Resource resource = new ClassPathResource("/data/authorities.csv");
        List<String> lines;
        try {
            Path path = Paths.get(resource.getURI());
            lines = Files.readAllLines(path);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        for (String line : lines) {
            if (Strings.isNullOrEmpty(line)) {
                continue;
            }
            String[] props = line.split(";");
            Authority authority = new Authority();
            authority.setAuthority(props[0].trim());
            authority.setName(props[1].trim());
            list.add(authority);
            if (map.containsKey(authority.getAuthority())) {
                throw new ServiceException("Authority '" + authority.getAuthority() + "' exists in csv file");
            }
            map.put(authority.getAuthority(), authority);
        }
        this.list = list;
    }

    public List<Authority> getList() {
        return this.list;
    }

    public Authority getByAuthority(String authority) {
        return map.get(authority);
    }
}
