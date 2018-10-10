package com.seagull.beedo.config;

import com.seagull.beedo.dao.mongodb.OptMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * SpringBoot2.0 已经不支持yaml配置授权路径
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/*")
                .authenticated();
    }

    @Bean
    public OptMongo optMongo(MongoDbFactory mongoDbFactory, MongoConverter converter) {
        return new OptMongo(mongoDbFactory, converter);
    }

}
