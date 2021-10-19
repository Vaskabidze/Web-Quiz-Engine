package engine.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeDef(name = "list", typeClass = ArrayList.class)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank
    @Column(name = "title")
    private String title;
    @NotBlank
    @Column(name = "text")
    private String text;
    @NotEmpty
    @Size(min = 2)
    @Column(name = "options")
    @Type(type = "list")
    private List<String> options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "answer")
    @Type(type = "list")
    private List<Integer> answer = new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
