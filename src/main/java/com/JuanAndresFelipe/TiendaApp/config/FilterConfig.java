package com.JuanAndresFelipe.TiendaApp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.JuanAndresFelipe.TiendaApp.filter.JwtValidationFilter;

@Configuration 
public class FilterConfig {
    
    /**
     * Método que coordina en qué orden se deben aplicar los filtros en la aplicación.
     * @param jwtValidationFilter
     * @return un Bean de registro de un filtro, el cual, está compuesto por un "Filtro validador de JWT."
     */
    @Bean
    FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValidationFilter) {
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtValidationFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        
        return registrationBean;
    }
}
