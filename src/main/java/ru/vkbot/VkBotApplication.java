package ru.vkbot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Random;

public class VkBotApplication {

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        VkApiClient api = new VkApiClient(new HttpTransportClient());
        Integer groupId = 226057952;
        String accessToken = "vk1.a.3fQa1_ojpYrHtocxiM1GwKssk_IEm-YYKrFQEbMRa5w72Erg3vhFQk0wF5i7ayklazT6UyyUQfUtLETvwauy7yJrq0Yt4D8FIo8_9-s-Kus3FGpZhtfQINYi0qiDHa_FIE-xGDpAxw-41QJ8vCoRzxxtypSP-KInalDMKZwLn02h1S895JW6-7xI_20vnosVULXN2yYEhXTshJEpey0Big";
        GroupActor actor = new GroupActor(groupId, accessToken);
        Integer ts = api.messages().getLongPollServer(actor).execute().getTs();
        while (true) {
            MessagesGetLongPollHistoryQuery hq = api.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = hq.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    System.out.println(message);
                    try {
                        String inputMessage = message.getText();
                        api.messages()
                                .send(actor)
                                .message("Вы сказали: " + inputMessage)
                                .userId(message.getFromId())
                                .randomId(new Random().nextInt())
                                .execute();
                    } catch (ApiException | ClientException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
            ts = api.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}
