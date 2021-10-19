package engine.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    public final static Answer CORRECT_ANSWER = new Answer(true, "Congratulations, you're right!");
    public final static Answer INCORRECT_ANSWER = new Answer(false, "Wrong answer! Please, try again.");
    private boolean success;
    private String feedback;
}
