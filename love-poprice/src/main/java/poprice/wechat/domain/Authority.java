package poprice.wechat.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import java.io.Serializable;

public class Authority implements GrantedAuthority, Serializable {

    private String authority;

    private String name;


    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        if (!StringUtils.equals(authority.getName(), this.getName())) {
            return false;
        }
        if (!StringUtils.equals(authority.getAuthority(), this.getAuthority())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.getAuthority());
        hashCodeBuilder.append(this.getName());
        return hashCodeBuilder.toHashCode();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authority='" + authority + '\'' +
                "name='" + name + '\'' +
                "}";
    }
}
