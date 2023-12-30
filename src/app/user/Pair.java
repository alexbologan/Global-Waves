package app.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }
}

