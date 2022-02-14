import org.apache.commons.lang3.RandomStringUtils;

public class UserWithEmailFieldOnly {
    public final String email;


    public UserWithEmailFieldOnly(String email){
        this.email = email;
    }

    public static UserWithEmailFieldOnly getUserWithEmailField(){
        final String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        return new UserWithEmailFieldOnly(email);
    }
}
