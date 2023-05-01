package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Mpa {
    private int id;
    private String name;

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa(int id) {
        this.id = id;
        this.name = "";
    }
}
