package pro.sky.recommendation.system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.recommendation.system.service.ProductCheckerService;
import pro.sky.recommendation.system.service.RecommendationService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class RecommendationControllerJUnitTest {

    @InjectMocks
    private RecommendationController recommendationController;

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private ProductCheckerService productCheckerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalDepositsByProductTypeValid() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String productType = "DEBIT";

        ResponseEntity<Object> response = recommendationController.getTotalDepositsByProductType(userId, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().equals(200.0));
    }

@Test
void testGetTotalDepositsByProductTypeInvalid() {
    UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb60");
    String invalidProductType = "INVALID_PRODUCT";

    when(productCheckerService.getTotalDepositsByProductType(userId, invalidProductType)).thenReturn(null);

    ResponseEntity<Object> response = recommendationController.getTotalDepositsByProductType(userId, invalidProductType);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
}

    @Test
    void testGetTotalDepositsByProductTypeNullProductType() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String nullProductType = null;

        ResponseEntity<Object> response = recommendationController.getTotalDepositsByProductType(userId, nullProductType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetTotalDepositsByProductTypeNullUserId() {
        String productType = "DEBIT";

        ResponseEntity<Object> response = recommendationController.getTotalDepositsByProductType(null, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetTotalWithdrawalsByProductTypeValid() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String productType = "DEBIT";

        ResponseEntity<Object> response = recommendationController.getTotalWithdrawalsByProductType(userId, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().equals(0.0));
    }

    @Test
    void testGetTotalWithdrawalsByProductTypeInvalid() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String invalidProductType = "INVALID_PRODUCT";

        when(productCheckerService.getTotalWithdrawalsByProductType(userId, invalidProductType)).thenReturn(null);

        ResponseEntity<Object> response = recommendationController.getTotalWithdrawalsByProductType(userId, invalidProductType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetTotalWithdrawalsByProductTypeNullProductType() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String nullProductType = null;

        ResponseEntity<Object> response = recommendationController.getTotalWithdrawalsByProductType(userId, nullProductType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetTotalWithdrawalsByProductTypeNullUserId() {
        String productType = "DEBIT";

        ResponseEntity<Object> response = recommendationController.getTotalWithdrawalsByProductType(null, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetTotalWithdrawalsByProductTypeNoData() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String productType = "DEBIT";

        ResponseEntity<Object> response = recommendationController.getTotalWithdrawalsByProductType(userId, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().equals(0.0));
    }
}
