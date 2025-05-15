package dev.kosmoos.kairosapi.entity;

import dev.kosmoos.kairosapi.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 99, unique = true, nullable = false)
    private String mail;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Role role;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 255)
    private String iconLink;

    @Column(length = 255)
    private String quote;

    @Column(length = 255)
    private String color;

    public User() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getIconLink() { return iconLink; }
    public void setIconLink(String iconLink) { this.iconLink = iconLink; }

    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

}