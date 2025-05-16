package dev.kosmoos.kairosapi.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class EventDTO {
    private Integer id;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private String title;
    private String description;
    private Integer creatorId;
    private List<Integer> attendeeIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(OffsetDateTime startAt) {
        this.startAt = startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(OffsetDateTime endAt) {
        this.endAt = endAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public List<Integer> getAttendeeIds() {
        return attendeeIds;
    }

    public void setAttendeeIds(List<Integer> attendeeIds) {
        this.attendeeIds = attendeeIds;
    }
}