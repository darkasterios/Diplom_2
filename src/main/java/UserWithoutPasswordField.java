import org.apache.commons.lang3.RandomStringUtils;

public class UserWithoutPasswordField {
    public final String email;
    public final String name;

    public UserWithoutPasswordField(String email,String name){
        this.email = email;
        this.name = name;
    }
    public static UserWithoutPasswordField getUserWithoutPassword(){
        final String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new UserWithoutPasswordField(email, name);
    }
}
