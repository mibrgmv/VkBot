package ru.vkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Класс, обрабатывающий сообщения получаемый ботом
 */
@Service
@Log4j2
public class VkService {

    /**
     * Экземпляр VkVpiClient для взаимодействия с VK API
     */
    private final VkApiClient vkApiClient;
    /**
     * Экземпляр GroupActor представляющий сконфигурированную группу ВК
     */
    private final GroupActor groupActor;
    /**
     * Последнее полученное значние ts, используемое для получения сообщений
     */
    private Integer ts;

    public VkService(VkApiClient vkApiClient, GroupActor groupActor) {
        this.vkApiClient = vkApiClient;
        this.groupActor = groupActor;
        this.ts = getTs();
    }

    /**
     * Каждые полсекунды метод проверяет чат и если в нём появились новые сообщения –
     * обрабатывает их и формирует ответ
     */
    @Scheduled(fixedRate = 500)
    public void processMessages() throws ClientException, ApiException {
        List<MessagePojo> messages = convertVkMessages(getLongPollHistory(ts));
        if (!messages.isEmpty()) {
            messages.forEach(message -> {
                log.info("Received message " + message.getId());
                try {
                    String responseMessage = processMessage(message.getText());
                    sendMessage(responseMessage, message.getFromId());
                } catch (ApiException | ClientException e) {
                    log.error("Error processing message: " + e.getMessage());
                }
            });
        }
        ts = getTs();
    }

    private Integer getTs() {
        try {
            return vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
        } catch (ApiException | ClientException e) {
            log.error("Error getting timestamp: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<Message> getLongPollHistory(Integer ts) throws ApiException, ClientException {
        MessagesGetLongPollHistoryQuery historyQuery = vkApiClient.messages().getLongPollHistory(groupActor).ts(ts);
        return historyQuery.execute().getMessages().getItems();
    }

    private List<MessagePojo> convertVkMessages(List<Message> vkMessages) {
        return vkMessages.stream().map(vkMessage -> new MessagePojo(
                vkMessage.getId(),
                vkMessage.getFromId(),
                vkMessage.getText()
        )).toList();
    }

    private String processMessage(String message) {
        return "Вы сказали: " + message;
    }

    private void sendMessage(String message, Integer userId) throws ApiException, ClientException {
        vkApiClient.messages()
                .send(groupActor)
                .message(message)
                .userId(userId)
                .randomId(new Random().nextInt())
                .execute();
    }
}