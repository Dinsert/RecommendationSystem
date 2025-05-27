package pro.sky.recommendation.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import pro.sky.recommendation.system.bot.RecommendationBot;

@Configuration
public class TelegramBotConfig {

    /**
     * Конфигурационный метод для регистрации бота Telegram и возвращения объекта {@code TelegramBotsApi},
     * позволяющего взаимодействовать с платформой Telegram.
     *
     * <p>Создает новый экземпляр TelegramBotsApi с типом сессии по умолчанию ({@code DefaultBotSession}),
     * регистрирует переданный бот {@code recommendationBot} и возвращает готовый объект.</p>
     *
     * @param recommendationBot экземпляр бота Telegram, который нужно зарегистрировать
     * @return сконфигурированный объект {@code TelegramBotsApi}, позволяющий управлять ботом
     * @throws TelegramApiException если возникают ошибки при регистрации бота
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(RecommendationBot recommendationBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(recommendationBot);
        } catch (TelegramApiException e) {
            throw e;
        }
        return botsApi;
    }
}