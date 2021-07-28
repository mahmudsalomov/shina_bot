package uz.shina.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.shina.bot.bot.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "telegram_user", uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id", name = "users_unique_chatid_idx")})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Integer chatId;

    @Column(name = "name")
//    @NotBlank
    private String name;

    @Column(name = "username")
//    @NotBlank
    private String username;

    private String firstname;

    private String lastname;

    private String action;

    private String language;

    private String phone;

    @Column(name = "bot_state", nullable = false)
    private State botState;

    @Column(name = "current_category_id", nullable = false)
    private Integer current_category_id;


    public User(int chatId) {
        this.chatId = chatId;
        this.name= String.valueOf(chatId);
        this.botState = State.START;
        this.current_category_id=1;
        this.action="";
    }

    public User(org.telegram.telegrambots.meta.api.objects.User user) {
        this.chatId = user.getId();
        this.name= String.valueOf(user.getId());
        this.username=user.getUserName();
        this.firstname=user.getFirstName();
        this.lastname=user.getLastName();
        this.botState = State.START;
        this.current_category_id=1;
    }


}
