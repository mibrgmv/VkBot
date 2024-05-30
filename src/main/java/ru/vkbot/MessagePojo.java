package ru.vkbot;

import lombok.Data;
import lombok.NonNull;

/**
 *   Класс представляет сообщение, получаенное ботом
 */
@Data
public class MessagePojo {
    @NonNull private Integer id;
    @NonNull private Integer fromId;
    @NonNull private String text;
}
