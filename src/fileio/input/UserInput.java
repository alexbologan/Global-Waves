package fileio.input;

import lombok.Getter;

@Getter
public final class UserInput {
    private String username;
    private String type;
    private int age;
    private String city;

    public UserInput() {
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserInput{"
                + "username='" + username + '\''
                + ", age=" + age
                + ", city='" + city + '\''
                + '}';
    }
}
