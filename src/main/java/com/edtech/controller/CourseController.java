package com.edtech.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import com.edtech.dto.CourseRequest;
import com.edtech.dto.CourseResponse;
import com.edtech.exception.ResourceNotFoundException;
import com.edtech.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
	private final CourseService courseService;

	    @PostMapping
	    @PreAuthorize("hasRole('INSTRUCTOR')")
	    public ResponseEntity<CourseResponse> addCourse(@RequestBody CourseRequest request) {
	        return ResponseEntity.ok(courseService.addCourse(request));
	    }

	    @GetMapping
	    public ResponseEntity<List<CourseResponse>> getAllCourses() {
	        return ResponseEntity.ok(courseService.getAllCourses());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) throws ResourceNotFoundException {
	        return ResponseEntity.ok(courseService.getCourseById(id));
	    }
	    
	    @PutMapping("/{id}")
	    @PreAuthorize("hasRole('INSTRUCTOR')")
	    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @RequestBody CourseRequest request) throws ResourceNotFoundException {
	        return ResponseEntity.ok(courseService.updateCourse(id, request));
	    }

	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('INSTRUCTOR')")
	    public ResponseEntity<String> deleteCourse(@PathVariable Long id) throws ResourceNotFoundException {
	        courseService.deleteCourse(id);
	        return ResponseEntity.ok("Course deleted successfully.");
	    }

}
