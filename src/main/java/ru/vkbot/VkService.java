package ru.vkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class VkService {

    private final VkApiClient vkApiClient;
    private final GroupActor groupActor;

    public VkService(VkApiClient vkApiClient, GroupActor groupActor) {
        this.vkApiClient = vkApiClient;
        this.groupActor = groupActor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void processMessages() throws ClientException, ApiException, InterruptedException {
        Integer ts = getTs();
        while (true) {
            List<MessagePojo> messages = convertVkMessages(getLongPollHistory(ts));
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    String inputMessage = message.getText();
                    try {
                        sendMessage("Вы сказали: " + inputMessage, message.getFromId());
                    } catch (ApiException | ClientException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            ts = getTs();
            Thread.sleep(500);
        }
    }

    private Integer getTs() throws ClientException, ApiException {
        return vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
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

    public void sendMessage(String message, Integer userId) throws ApiException, ClientException {
        vkApiClient.messages()
                .send(groupActor)
                .message(message)
                .userId(userId)
                .randomId(new Random().nextInt())
                .execute();
    }
}