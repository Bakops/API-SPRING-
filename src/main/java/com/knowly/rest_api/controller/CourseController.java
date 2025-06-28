package com.knowly.rest_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowly.rest_api.controller.request.NewCourseRequest;
import com.knowly.rest_api.entity.Course;
import com.knowly.rest_api.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addCourse(@RequestBody NewCourseRequest request) {
        try {
            Long newId = courseService.addCourse(request);
            return ResponseEntity.status(201).body("Course created with ID: " + newId);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to create course: " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateCourse(@RequestBody NewCourseRequest request, @PathVariable Long id) {
        try {
            courseService.updateCourse(id, request);
            return ResponseEntity.ok("Course updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to update course: " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete course: " + e.getMessage());
        }
    }
}
