package ru.vkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Класс с конфигурацией приложения
 */
@Configuration
@PropertySource(value = "classpath:vk.properties")
@EnableScheduling
public class VkConfig {

    /**
     * Достаём ID группы из файла vk.properties
     */
    @Value("${group.id}")
    private Integer groupId;

    /**
     * Достаём токен доступа из файла vk.properties
     */
    @Value("${access.token}")
    private String accessToken;

    @Bean
    public VkApiClient vkApiClient() {
        return new VkApiClient(new HttpTransportClient());
    }

    @Bean
    public GroupActor groupActor() {
        return new GroupActor(groupId, accessToken);
    }
}
