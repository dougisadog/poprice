package poprice.wechat.config;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import poprice.wechat.security.AuthoritiesConstants;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Environment env;

    @Inject
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                //.antMatchers("/scripts/**/*.{js,html}")
                .antMatchers("/api/**")
                .antMatchers("/static/**")
                .antMatchers("/i18n/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()

                .and()
                .formLogin()
                .loginPage("/auth/index")
                //下面这一行乱改,主要是为了安全问题,防止攻击
                .loginProcessingUrl("/auth/login-xtxxxxcq1ccd")
                .defaultSuccessUrl("/auth/dispatch")
                //.failureHandler(new HttpAuthenticationFailureHandler("/auth/index?error"))
                //.successHandler(new HttpAuthenticationSuccessHandler())
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll()
                .and()
                .logout()
                //.logoutUrl("/auth/logout") //To prevent csrf this only allowed post logout, http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/#csrf-logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                //.logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index*").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/**/authenticate").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/file/download/static/**").permitAll()
                .antMatchers("/customer/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/account/**").hasAuthority(AuthoritiesConstants.ADMIN) //管理员部分
                .antMatchers("/base/announcement/**").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.SERVICE)
                .antMatchers("/base/processor/**").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.SERVICE)
                .antMatchers("/base/configuration/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin*").authenticated();

    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }



}
