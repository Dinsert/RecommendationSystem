# Recommendation System

Сервис рекомендаций для пользователей банка. Предоставляет персонализированные рекомендации через REST API и телеграм-бота, поддерживает динамические правила, собирает статистику их срабатываний и предоставляет возможности управления сервисом (например, сброс кеша и получение информации о сервисе).

## Документация
- [Требования](https://github.com/Dinsert/RecommendationSystem/wiki/Requirements)
- [Архитектура](https://github.com/Dinsert/RecommendationSystem/wiki/Architecture)
- [Отслеживание требований](https://github.com/Dinsert/RecommendationSystem/wiki/Requirements_tracking)
- [API-документация](https://github.com/Dinsert/RecommendationSystem/wiki/API)
- [Инструкция по развертыванию](https://github.com/Dinsert/RecommendationSystem/wiki/Deployment)

## Технологии
- Java 17, Spring Boot
- PostgreSQL (правила), H2 (транзакции)
- Liquibase (миграции БД)
- Spring Cache (кэширование)
- OpenAPI (API-документация)
- Telegram Bot API
- JUnit (тесты)

## Quick Start
1. Убедитесь, что у вас установлены Java 17 и PostgreSQL (версия 13 или выше).
2. Склонируйте репозиторий:
```bash
git clone https://github.com/Dinsert/RecommendationSystem
```
1. Перейдите в директорию проекта:
```bash
cd recommendation-system
```
2. Выполните сборку:
```bash
./mvnw package
```
3. Создайте базу данных rules_db в PostgreSQL:
```bash
psql -U username -c "CREATE DATABASE rules_db;"
```
4. Запустите приложение, задав переменные среды:
```bash
SPRING_SECOND_DATASOURCE_URL=jdbc:postgresql://localhost:5432/rules_db \ 
SPRING_SECOND_DATASOURCE_USERNAME=username \ 
SPRING_SECOND_DATASOURCE_PASSWORD=password \ 
SPRING_LIQUIBASE_URL=jdbc:postgresql://localhost:5432/rules_db \ 
SPRING_LIQUIBASE_USER=username \ 
SPRING_LIQUIBASE_PASSWORD=password \ 
TELEGRAM_BOT_USERNAME=RecommendationTeam3Bot \ 
TELEGRAM_BOT_TOKEN=8039753322:AAFFl6Xhppq0s5M4ccVQ5HQWqH0uj9vegEw \ 
java -jar target/recommendation-system-1.0.0.jar
```

Подробная инструкция по развертыванию доступна в [Deployment.md](https://github.com/Dinsert/RecommendationSystem/wiki/Deployment).