package dev.kosmoos.kairosapi.dto;

import dev.kosmoos.kairosapi.Role;
import java.util.List;

public class UserDTO {
    private Integer id;
    private String name;
    private String mail;
    private Role role;
    private String iconLink;
    private String quote;
    private String color;
    private List<Integer> eventsCreatedIds;
    private List<Integer> eventsAttendedIds;

    public UserDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getEventsAttendedIds() {
        return eventsAttendedIds;
    }

    public void setEventsAttendedIds(List<Integer> eventsAttendedIds) {
        this.eventsAttendedIds = eventsAttendedIds;
    }

    public List<Integer> getEventsCreatedIds() {
        return eventsCreatedIds;
    }

    public void setEventsCreatedIds(List<Integer> eventsCreatedIds) {
        this.eventsCreatedIds = eventsCreatedIds;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}