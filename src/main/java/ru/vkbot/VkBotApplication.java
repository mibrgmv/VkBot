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
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@SpringBootApplication
public class VkBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(VkBotApplication.class, args);
    }

//    public void responseMessages() throws ClientException, ApiException, IOException {
//        VkApiClient api = new VkApiClient(new HttpTransportClient());
//
//        Properties properties = new Properties();
//        properties.load(new FileInputStream("src/main/resources/vk.properties"));
//        Integer groupId = Integer.parseInt(properties.getProperty("groupId"));
//        String accessToken = properties.getProperty("accessToken");
//
//        GroupActor actor = new GroupActor(groupId, accessToken);
//        Integer ts = api.messages().getLongPollServer(actor).execute().getTs();
//        MessagesGetLongPollHistoryQuery hq = api.messages().getLongPollHistory(actor).ts(ts);
//        List<Message> messages = hq.execute().getMessages().getItems();
//        if (!messages.isEmpty()) {
//            messages.forEach(message -> {
//                System.out.println(message);
//                try {
//                    String inputMessage = message.getText();
//                    api.messages()
//                            .send(actor)
//                            .message("Вы сказали: " + inputMessage)
//                            .userId(message.getFromId())
//                            .randomId(new Random().nextInt())
//                            .execute();
//                } catch (ApiException | ClientException e) {
//                    System.out.println(e.getMessage());
//                }
//            });
//        }
//    }
}
