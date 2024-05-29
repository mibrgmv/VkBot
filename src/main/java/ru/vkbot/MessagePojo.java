package ru.vkbot;

import lombok.Data;
import lombok.NonNull;

@Data
public class MessagePojo {
    @NonNull private Integer id;
    @NonNull private Integer fromId;
    @NonNull private String text;
}
