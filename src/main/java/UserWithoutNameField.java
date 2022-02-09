import org.apache.commons.lang3.RandomStringUtils;

public class UserWithoutNameField {
    public final String email;
    public final String password;

    public UserWithoutNameField(String email, String password){
        this.email = email;
        this.password = password;
    }

    public static UserWithoutNameField getUserWithoutName(){
        final String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        return new UserWithoutNameField(email, password);
    }

    public static UserWithoutNameField getUsersEmailAndPassword(User user){
        return new UserWithoutNameField(user.email, user.password);
    }

}
