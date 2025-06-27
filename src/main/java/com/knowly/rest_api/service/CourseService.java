
package com.knowly.rest_api.service;

import java.util.ArrayList;
import java.util.List;
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
        Course course = new Course();
        course.setId(courseId);
        course.setName(request.name());
        course.setPrice(request.price() != null ? request.price().doubleValue() : null);
        redisTemplate.opsForValue().set(COURSE_KEY_PREFIX + courseId, course);
        return courseId;
    }

    public Course getCourseById(Long id) {
        Object obj = redisTemplate.opsForValue().get(COURSE_KEY_PREFIX + id);
        if (obj instanceof Course course) {
            return course;
        } else {
            throw new RuntimeException("Course not found with ID: " + id);
        }
    }

    public List<Course> getAllCourses() {
        Set<String> keys = redisTemplate.keys(COURSE_KEY_PREFIX + "*");
        List<Course> courses = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                Object obj = redisTemplate.opsForValue().get(key);
                if (obj instanceof Course course) {
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    public void updateCourse(Long id, NewCourseRequest request) {
        Object obj = redisTemplate.opsForValue().get(COURSE_KEY_PREFIX + id);
        if (obj instanceof Course course) {
            course.setName(request.name());
            course.setPrice(request.price() != null ? request.price().doubleValue() : null);
            redisTemplate.opsForValue().set(COURSE_KEY_PREFIX + id, course);
        } else {
            throw new RuntimeException("Course not found with ID: " + id);
        }
    }

    public void deleteCourse(Long id) {
        redisTemplate.delete(COURSE_KEY_PREFIX + id);
    }
}