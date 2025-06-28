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

import com.knowly.rest_api.controller.request.NewLessonRequest;
import com.knowly.rest_api.entity.Lesson;
import com.knowly.rest_api.service.LessonService;

@RestController
@RequestMapping("/courses")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> getLessonsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getLessonsByCourseId(courseId));
    }

    @GetMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long courseId, @PathVariable Long lessonId) {
        try {
            Lesson lesson = lessonService.getLessonById(lessonId);
            return ResponseEntity.ok(lesson);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<String> addLesson(@RequestBody NewLessonRequest request, @PathVariable Long courseId) {
        try {
            lessonService.addLesson(request, courseId);
            return ResponseEntity.status(201).body("Lesson created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to create lesson: " + e.getMessage());
        }
    }

    @PutMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<String> updateLesson(@RequestBody NewLessonRequest request, @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        try {
            lessonService.updateLesson(lessonId, request);
            return ResponseEntity.ok("Lesson updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to update lesson: " + e.getMessage());
        }
    }

    @DeleteMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        try {
            lessonService.deleteLesson(lessonId, courseId);
            return ResponseEntity.ok("Lesson deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete lesson: " + e.getMessage());
        }
    }
}
