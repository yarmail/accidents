package ru.job4j.accidents.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.sql.DataSource;

/**
 * configure(Auth...
 * содержит описание, как искать пользователей.
 * В этом примере мы загружаем их в память.
 *
 * У каждого пользователя есть роль.
 * По роли мы определяем, что пользователь может делать .
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    DataSource ds;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(ds)
                .usersByUsernameQuery("select username, password, enabled "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery(
                        " select u.username, a.authority "
                                + "from authorities as a, users as u "
                                + "where u.username = ? and u.authority_id = a.id");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/reg")
                .permitAll()
                .antMatchers("/**")
                .hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .csrf()
                .disable();
    }
}
/* Примечания
Метод configure(http) содержит описание доступов
и конфигурирование страницы входа в приложение.

- ссылки, которые доступны всем.
.antMatchers("/login")
.permitAll()

- ссылки доступны только пользователем с ролями ADMIN, USER.
.antMatchers("/**")
.hasAnyRole("ADMIN", "USER")

Настройка формы авторизации.
.formLogin()
.loginPage("/login")
.defaultSuccessUrl("/")
.failureUrl("/login?error=true")
.permitAll()

Конфигурация без БД
configure(Auth)
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("user")
                .password(passwordEncoder.encode("123456")).roles("USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder.encode("123456")).roles("USER", "ADMIN");

Конфигурация с БД
(По умолчанию мы добавляем пользователя user с паролем 123456)
        auth.jdbcAuthentication()
                .dataSource(ds)
                .withUser(User.withUsername("user")
                        .password(passwordEncoder.encode("123456"))
                        .roles("USER"));
ВНИМАНИЕ
При каждом вызове программы в таблицу загоняется пользователь
по умолчанию со своим паролем
При повторном вызове программы нужно удалять эту запись,
т.к. она повторяется и мешает.

Конфигурация с БД 2
        auth.jdbcAuthentication().dataSource(ds)
                .usersByUsernameQuery("select username, password, enabled "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery(
                        " select u.username, a.authority "
                                + "from authorities as a, users as u "
                                + "where u.username = ? and u.authority_id = a.id");
*/