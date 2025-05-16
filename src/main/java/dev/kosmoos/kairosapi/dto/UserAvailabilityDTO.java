package dev.kosmoos.kairosapi.dto;

import java.time.OffsetDateTime;

public class UserAvailabilityDTO {
    private Integer id;
    private Integer userId;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private String comment;

    public UserAvailabilityDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public OffsetDateTime getStartAt() { return startAt; }
    public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }

    public OffsetDateTime getEndAt() { return endAt; }
    public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
