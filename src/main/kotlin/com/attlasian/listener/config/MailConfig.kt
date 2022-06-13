package com.attlasian.listener.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean

@Configuration
class MailConfig {
    @Bean
    @Qualifier("freeMarker")
    fun getFreeMarkerConfiguration(): FreeMarkerConfigurationFactoryBean? {
        val bean = FreeMarkerConfigurationFactoryBean()
        bean.setTemplateLoaderPath("classpath:/templates/")
        return bean
    }
}
