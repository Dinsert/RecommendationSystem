package pro.sky.recommendation.system.DTO;

import java.util.List;
import java.util.UUID;

public class UserInfo {

    private final UUID userId;
    private final String firstName;
    private final String lastName;
    private final List<RecommendationDTO> recommendations;

    public UserInfo(UUID userId, String firstName, String lastName, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.recommendations = recommendations;
    }

    public UUID getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public List<RecommendationDTO> getRecommendations() { return recommendations; }
}
