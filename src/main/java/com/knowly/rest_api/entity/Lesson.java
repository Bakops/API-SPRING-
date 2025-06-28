package com.knowly.rest_api.entity;

import java.io.Serializable;

public class Lesson implements Serializable {
    private Long id;
    private String content;
    private Long courseId;

    public Lesson() {
    }

    public Lesson(Long id, String content, Long courseId) {
        this.id = id;
        this.content = content;
        this.courseId = courseId;
    }

    public Lesson(String content, Long courseId) {
        this.content = content;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
