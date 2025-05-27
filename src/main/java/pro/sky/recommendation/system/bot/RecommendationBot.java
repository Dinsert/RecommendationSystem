package pro.sky.recommendation.system.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.DTO.UserInfo;
import pro.sky.recommendation.system.exception.UserNotFoundException;
import pro.sky.recommendation.system.service.RecommendationService;

/**
 * Бот для Telegram, который предоставляет рекомендации пользователям.
 */
@Component
public class RecommendationBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationBot.class);
    private final RecommendationService recommendationService;
    private final String botUsername;
    private final String botToken;

    /**
     * Конструктор бота рекомендаций.
     *
     * @param recommendationService сервис для получения рекомендаций
     * @param botUsername имя бота (из конфигурации)
     * @param botToken токен бота (из конфигурации)
     */
    public RecommendationBot(RecommendationService recommendationService,
                             @Value("${telegram.bot.username}") String botUsername,
                             @Value("${telegram.bot.token}") String botToken) {
        this.recommendationService = recommendationService;
        this.botUsername = botUsername;
        this.botToken = botToken;
        logger.info("RecommendationBot initialized with username: {} and token: {}", botUsername, botToken);
    }

    /**
     * Возвращает имя бота.
     *
     * @return имя бота, указанное при конфигурации
     */
    @Override
    public String getBotUsername() {
        logger.debug("Returning bot username: {}", botUsername);
        return botUsername;
    }

    /**
     * Возвращает токен бота.
     *
     * @return токен бота, указанный при конфигурации
     */
    @Override
    public String getBotToken() {
        logger.debug("Returning bot token: {}", botToken);
        return botToken;
    }

    /**
     * Обрабатывает входящие обновления от Telegram API.
     * Выполняет обработку команд, отправляемых пользователями.
     * Поддерживаемые команды:
     *   - "/start": приветствие и инструкция по использованию бота.
     *   - "/recommend <username>": выводит персональные рекомендации для указанного пользователя.
     *
     * Логика обработки сообщений включает проверку валидности входящих данных и формирование ответных сообщений.
     *
     * @param update объект Update, содержащий информацию о сообщении или событии от Telegram API
     */
    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Received update: {}", update);
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            logger.debug("Update does not contain message or text, skipping");
            return;
        }

        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        logger.info("Message text: {}, Chat ID: {}", messageText, chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (messageText.equals("/start")) {
            logger.info("Processing /start command");
            message.setText("Привет! Я бот рекомендаций.\n" +
                    "Используй команду: /recommend <username>\n" +
                    "Например: /recommend john_doe");
        } else if (messageText.startsWith("/recommend ")) {
            logger.info("Processing /recommend command");
            String username = messageText.substring("/recommend ".length()).trim();
            logger.info("Username: {}", username);
            try {
                UserInfo userInfo = recommendationService.getRecommendationsByUsername(username);
                StringBuilder response = new StringBuilder();
                response.append("Здравствуйте ").append(userInfo.getFirstName())
                        .append(" ").append(userInfo.getLastName()).append("\n");
                response.append("Новые продукты для вас:\n");
                for (int i = 0; i < userInfo.getRecommendations().size(); i++) {
                    RecommendationDTO dto = userInfo.getRecommendations().get(i);
                    response.append(String.format("%d. %s: %s%n", i + 1, dto.getName(), dto.getText()));
                }
                message.setText(response.toString());
            } catch (UserNotFoundException e) {
                logger.warn("User not found: {}", username);
                message.setText("Пользователь не найден");
            } catch (Exception e) {
                logger.error("Error processing /recommend: {}", e.getMessage(), e);
                message.setText("Произошла ошибка");
            }
        } else {
            logger.info("Unknown command: {}", messageText);
            message.setText("Неизвестная команда. Используй /recommend <username>");
        }

        try {
            execute(message);
            logger.info("Message sent: {}", message.getText());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}