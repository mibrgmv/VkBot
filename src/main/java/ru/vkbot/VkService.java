package ru.vkbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
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

//    @Scheduled(cron= "0/1 * * ? * *")
    @EventListener(ApplicationReadyEvent.class)
    public void processMessages() throws ClientException, ApiException, InterruptedException {
        Integer ts = vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
        while (true) {
            MessagesGetLongPollHistoryQuery hq = vkApiClient.messages().getLongPollHistory(groupActor).ts(ts);
            List<Message> messages = hq.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    System.out.println(message);
                    try {
                        String inputMessage = message.getText();
                        vkApiClient.messages()
                                .send(groupActor)
                                .message("Вы сказали: " + inputMessage)
                                .userId(message.getFromId())
                                .randomId(new Random().nextInt())
                                .execute();
                    } catch (ApiException | ClientException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
            ts = vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
            Thread.sleep(500);
        }
//
//    public Integer getTs() throws ClientException, ApiException {
//        return vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
//    }
//
//    public List<Message> getLongPollHistory(Integer ts) throws ApiException, ClientException {
//        MessagesGetLongPollHistoryQuery hq = vkApiClient.messages().getLongPollHistory(groupActor).ts(ts);
//        return hq.execute().getMessages().getItems();
//    }
//
//    public void sendMessage(String message, Integer userId) throws ApiException, ClientException {
//        vkApiClient.messages()
//                .send(groupActor)
//                .message(message)
//                .userId(userId)
//                .randomId(new Random().nextInt())
//                .execute();
    }
}