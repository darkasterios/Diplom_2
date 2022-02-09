import org.apache.commons.lang3.RandomStringUtils;

public class UserWithoutEmailField {
    public final String password;
    public final String name;

    public UserWithoutEmailField(String password,String name){
        this.password = password;
        this.name = name;
    }
    public static UserWithoutEmailField getUserWithoutEmailField(){
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new UserWithoutEmailField(password,name);
    }
}
