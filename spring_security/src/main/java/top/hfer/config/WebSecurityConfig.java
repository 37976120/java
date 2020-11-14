package top.hfer.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //让它知道去哪里查询用户信息
    @Bean
    UserDetailsService myUserDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
        manager.createUser(User.withUsername("lishi").password("123").authorities("p1").build());
        return manager;
    }

    //可以给一个密码编码器
    @Bean
    public PasswordEncoder myEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //定义它的拦截机制（没有登录让你登录，登录了了也要鉴权）

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/r/r1").hasAnyAuthority("p1")//鉴权
                .antMatchers("/").authenticated()  //所有请求必需认证
                .and()
                .formLogin();  //允许表单登录

//                .successForwardUrl("login-success"); //自定登录成功页面地址

    }
}
