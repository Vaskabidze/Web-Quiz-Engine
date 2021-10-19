package engine.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompletedQuestions {
    @JsonIgnore
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JsonProperty(value = "id")
    @Column(name = "testId")
    private int testId;
    @Column(name = "completedAt")
    private LocalDateTime completedAt;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CompletedQuestions(int testId, User user) {
        this.testId = testId;
        this.completedAt = LocalDateTime.now();
        this.user = user;
    }
}
