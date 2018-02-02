package poprice.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 * http://www.cnblogs.com/lich/archive/2011/12/03/2274882.html
 *
 */
@Entity
@Table(name = "ACCT_USER", indexes = {@Index(name="idx_user_username", columnList = "username", unique = true)})
public class User extends AbstractAuditingEntity implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "只允许大小写字母及数字")
    @Size(min = 1, max = 50)
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;


    /**
     * 默认给60个字符，防止校验失败
     */
    @JsonIgnore
    @NotBlank
    @NotNull
    @Size(min = 0, max = 60)
    @Column(length = 60)
    private String password = "111111111111111111111111111111111111111111111111111111111111";

    /**
     * 6-25, not blank
     */
    @Transient
    private String password1;

    @Size(max = 25)
    @Column(name = "name", length = 50)
    private String name;

    @Email
    @Size(max = 100)
    @NotNull
    @NotBlank
    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Size(max = 120)
    @Column(length = 255)
    private String remark;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;


    /**
     * 万般无奈加了这么个字段啊
     */
    @Column(nullable = false)
    private boolean adminFlag = false;

    /**
     * 是否删除
     */
    @Column(name = "deleted")
    private boolean deleted=false;


    @Transient
    private Set<GrantedAuthority> authorities = Sets.newLinkedHashSet();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "ACCT_USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @OrderBy("id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Role> roles = Sets.newLinkedHashSet();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersistentToken> persistentTokens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<PersistentToken> getPersistentTokens() {
        return persistentTokens;
    }

    public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
        this.persistentTokens = persistentTokens;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public boolean isAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void addToAuthorities(GrantedAuthority authority) {
        authorities.add(authority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(accountNonExpired, user.accountNonExpired) &&
                Objects.equal(accountNonLocked, user.accountNonLocked) &&
                Objects.equal(credentialsNonExpired, user.credentialsNonExpired) &&
                Objects.equal(enabled, user.enabled) &&
                Objects.equal(id, user.id) &&
                Objects.equal(username, user.username) &&
                Objects.equal(name, user.name) &&
                Objects.equal(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username, name, email, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", authorities=" + authorities +
                ", roles=" + roles +
                '}';
    }
}
