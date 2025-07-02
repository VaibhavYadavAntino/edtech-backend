package com.edtech.service;
import com.edtech.dto.CourseRequest;
import com.edtech.dto.CourseResponse;
import com.edtech.exception.ResourceNotFoundException;

import java.util.List;

public interface CourseService {
	CourseResponse addCourse(CourseRequest request);
	List<CourseResponse> getAllCourses();
	CourseResponse getCourseById(Long id) throws ResourceNotFoundException;
	CourseResponse updateCourse(Long id, CourseRequest request) throws ResourceNotFoundException;
	void deleteCourse(Long id) throws ResourceNotFoundException;

}
