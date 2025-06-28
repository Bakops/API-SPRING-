package com.knowly.rest_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String key = LESSON_KEY_PREFIX + lessonId;

        redisTemplate.opsForHash().put(key, "id", String.valueOf(lessonId));
        redisTemplate.opsForHash().put(key, "content", request.content());
        redisTemplate.opsForHash().put(key, "course_id", String.valueOf(courseId));

        redisTemplate.opsForList().rightPush(COURSE_LESSONS_KEY_PREFIX + courseId, lessonId.toString());
    }

    public Lesson getLessonById(Long id) {
        String key = LESSON_KEY_PREFIX + id;
        Map<Object, Object> fields = redisTemplate.opsForHash().entries(key);

        if (fields == null || fields.isEmpty()) {
            throw new RuntimeException("Lesson not found with ID: " + id);
        }

        Lesson lesson = new Lesson();
        lesson.setId(Long.valueOf(fields.get("id").toString()));
        lesson.setContent(fields.get("content").toString());

        Object courseId = fields.get("course_id");
        if (courseId != null) {
            lesson.setCourseId(Long.valueOf(courseId.toString()));
        }

        return lesson;
    }

    public List<Lesson> getLessonsByCourseId(Long courseId) {
        List<Object> lessonIds = redisTemplate.opsForList().range(COURSE_LESSONS_KEY_PREFIX + courseId, 0, -1);
        List<Lesson> lessons = new ArrayList<>();

        if (lessonIds != null) {
            for (Object idObj : lessonIds) {
                Long id = null;
                try {
                    id = Long.valueOf(idObj.toString());
                } catch (NumberFormatException e) {
                    continue;
                }

                Map<Object, Object> fields = redisTemplate.opsForHash().entries(LESSON_KEY_PREFIX + id);
                if (!fields.isEmpty()) {
                    Lesson lesson = new Lesson();
                    lesson.setId(Long.valueOf(fields.get("id").toString()));
                    lesson.setContent(fields.get("content").toString());

                    Object cid = fields.get("course_id");
                    if (cid != null) {
                        lesson.setCourseId(Long.valueOf(cid.toString()));
                    }

                    lessons.add(lesson);
                }
            }
        }

        return lessons;
    }

    public void updateLesson(Long lessonId, NewLessonRequest request) {
        String key = LESSON_KEY_PREFIX + lessonId;

        if (!redisTemplate.hasKey(key)) {
            throw new RuntimeException("Lesson not found with ID: " + lessonId);
        }

        redisTemplate.opsForHash().put(key, "content", request.content());
    }

    public void deleteLesson(Long lessonId, Long courseId) {
        redisTemplate.delete(LESSON_KEY_PREFIX + lessonId);
        redisTemplate.opsForList().remove(COURSE_LESSONS_KEY_PREFIX + courseId, 1, lessonId.toString());
    }
}
