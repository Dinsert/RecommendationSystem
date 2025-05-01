package pro.sky.recommendation.system.service.RuleSet;

import org.springframework.stereotype.Component;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.service.ProductCheckerService;
import pro.sky.recommendation.system.service.RecommendationRuleSet;

import java.util.Optional;
import java.util.UUID;

//Top Saving
//Пользователь использует как минимум один продукт с типом DEBIT.
//Сумма пополнений по всем продуктам типа DEBIT больше или равна 50 000 ₽ ИЛИ Сумма пополнений по всем продуктам типа SAVING больше или равна 50 000 ₽.
//Сумма пополнений по всем продуктам типа DEBIT больше, чем сумма трат по всем продуктам типа DEBIT.
@Component
public class TopSavingRuleImpl implements RecommendationRuleSet {

    private final ProductCheckerService productChecker;


    public TopSavingRuleImpl(ProductCheckerService productChecker) {
        this.productChecker = productChecker;
    }

    @Override
    public Optional<RecommendationDTO> checkRecommendation(UUID userId) {
        // Проверяем наличие DEBIT продукта
        if (!productChecker.hasProductType(userId, "DEBIT")) {
            return Optional.empty();
        }

        // Получаем суммы пополнений
        Double debitDeposits = productChecker.getTotalDepositsByProductType(userId, "DEBIT");
        Double savingDeposits = productChecker.getTotalDepositsByProductType(userId, "SAVING");

        // Проверяем условие: DEBIT >=50000 или SAVING >=50000
        if ((debitDeposits == null || debitDeposits < 50000) &&
                (savingDeposits == null || savingDeposits < 50000)) {
            return Optional.empty();
        }

        // Проверяем DEBIT deposits > DEBIT withdrawals
        Double debitWithdrawals = productChecker.getTotalWithdrawalsByProductType(userId, "DEBIT");
        if (debitDeposits == null || debitWithdrawals == null || debitDeposits <= debitWithdrawals) {
            return Optional.empty();
        }

        return Optional.of(new RecommendationDTO(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                "Top Saving",
                "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n" +
                        "Преимущества «Копилки»:" +
                        "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет." +
                        "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости." +
                        "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг." +
                        "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"
        ));
    }
}

