package io.github.ljezio.jwttoken;

import java.util.Objects;

public class Payload {
    public Payload() {
    }

    public Payload(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payload payload)) return false;
        return id == payload.id && Objects.equals(name, payload.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Payload{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
