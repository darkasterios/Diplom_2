import org.apache.commons.lang3.RandomStringUtils;

public class UserWithNameFieldOnly {
    public final String name;

    public UserWithNameFieldOnly(String name){
        this.name = name;
    }

    public static UserWithNameFieldOnly getUserWithNameField(){
        final String name = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        return new UserWithNameFieldOnly(name);
    }
}
