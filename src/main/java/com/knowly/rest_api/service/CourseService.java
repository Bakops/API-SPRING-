package com.knowly.rest_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.knowly.rest_api.controller.request.NewCourseRequest;
import com.knowly.rest_api.entity.Course;

@Service
public class CourseService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String COURSE_KEY_PREFIX = "course:";

    public Long addCourse(NewCourseRequest request) {
        Long courseId = System.currentTimeMillis();
        String key = COURSE_KEY_PREFIX + courseId;

        redisTemplate.opsForHash().put(key, "id", String.valueOf(courseId));
        redisTemplate.opsForHash().put(key, "name", request.name());
        redisTemplate.opsForHash().put(key, "price", String.valueOf(request.price()));

        return courseId;
    }

    public Course getCourseById(Long id) {
        String key = COURSE_KEY_PREFIX + id;
        Map<Object, Object> fields = redisTemplate.opsForHash().entries(key);

        if (fields == null || fields.isEmpty()) {
            throw new RuntimeException("Course not found with ID: " + id);
        }

        Course course = new Course();
        course.setId(Long.valueOf(fields.get("id").toString()));
        course.setName(fields.get("name").toString());

        Object price = fields.get("price");
        if (price != null) {
            course.setPrice(Double.valueOf(price.toString()));
        }

        return course;
    }

    public List<Course> getAllCourses() {
        Set<String> keys = redisTemplate.keys(COURSE_KEY_PREFIX + "*");
        List<Course> courses = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                Map<Object, Object> fields = redisTemplate.opsForHash().entries(key);
                if (!fields.isEmpty()) {
                    Course course = new Course();
                    course.setId(Long.valueOf(fields.get("id").toString()));
                    course.setName(fields.get("name").toString());

                    Object price = fields.get("price");
                    if (price != null) {
                        course.setPrice(Double.valueOf(price.toString()));
                    }

                    courses.add(course);
                }
            }
        }

        return courses;
    }

    public void updateCourse(Long id, NewCourseRequest request) {
        String key = COURSE_KEY_PREFIX + id;

        if (!redisTemplate.hasKey(key)) {
            throw new RuntimeException("Course not found with ID: " + id);
        }

        redisTemplate.opsForHash().put(key, "name", request.name());
        redisTemplate.opsForHash().put(key, "price", String.valueOf(request.price()));
    }

    public void deleteCourse(Long id) {
        redisTemplate.delete(COURSE_KEY_PREFIX + id);
    }
}
