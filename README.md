## Требования
- Java 8

## Инструменты 
- Spring Boot, VK API, lombok, Log4j

## Инструкция по запуску 

#### 1. Скопировать проект  
`git clone https://github.com/mibrgmv/VkBot.git`

#### 2. Подставить свои параметры в файл конфига
Файл `resources/vk.properties`
|свойство    |значение           |
|------------|------------------------|
| group.id   | адресс группы ВК       |
|access.token| токен доступа к группе | 

#### 3. Запустить приложение   
- Через IDE
- Через терминал `$ ./mvnw spring-boot:run`

## Пример работы
<img src="https://github.com/mibrgmv/VkBot/blob/main/example.png" alt="drawing" width="500"/>

