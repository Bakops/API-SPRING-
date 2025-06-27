package com.knowly.rest_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.knowly.rest_api.controller.request.NewLessonRequest;
import com.knowly.rest_api.entity.Lesson;

@Service
public class LessonService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LESSON_KEY_PREFIX = "lesson:";
    private static final String COURSE_LESSONS_KEY_PREFIX = "course_lessons:";

    public void addLesson(NewLessonRequest request, Long courseId) {
        Long lessonId = System.currentTimeMillis();
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setContent(request.content());
        lesson.setCourseId(courseId);

        redisTemplate.opsForValue().set(LESSON_KEY_PREFIX + lessonId, lesson);
        redisTemplate.opsForList().rightPush(COURSE_LESSONS_KEY_PREFIX + courseId, lessonId);
    }

    public Lesson getLessonById(Long id) {
        Lesson lesson = (Lesson) redisTemplate.opsForValue().get(LESSON_KEY_PREFIX + id);
        if (lesson != null) {
            return lesson;
        } else {
            throw new RuntimeException("Lesson not found with ID: " + id);
        }
    }

    public List<Lesson> getLessonsByCourseId(Long courseId) {
        List<Object> lessonIds = redisTemplate.opsForList().range(COURSE_LESSONS_KEY_PREFIX + courseId, 0, -1);
        List<Lesson> lessons = new ArrayList<>();
        if (lessonIds != null) {
            for (Object idObj : lessonIds) {
                Long id = (Long) idObj;
                Lesson lesson = (Lesson) redisTemplate.opsForValue().get(LESSON_KEY_PREFIX + id);
                if (lesson != null) {
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    public void updateLesson(Long lessonId, NewLessonRequest request) {
        Lesson lesson = (Lesson) redisTemplate.opsForValue().get(LESSON_KEY_PREFIX + lessonId);
        if (lesson != null) {
            lesson.setContent(request.content());
            redisTemplate.opsForValue().set(LESSON_KEY_PREFIX + lessonId, lesson);
        }
    }

    public void deleteLesson(Long lessonId, Long courseId) {
        redisTemplate.delete(LESSON_KEY_PREFIX + lessonId);
        redisTemplate.opsForList().remove(COURSE_LESSONS_KEY_PREFIX + courseId, 1, lessonId);
    }
}