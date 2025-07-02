package com.edtech.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edtech.dto.CourseRequest;
import com.edtech.dto.CourseResponse;
import com.edtech.entity.Course;
import com.edtech.exception.ResourceNotFoundException;
import com.edtech.repository.CourseRepository;
import com.edtech.service.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

	@Autowired
	private final CourseRepository courseRepository;

	@Override
	public CourseResponse addCourse(CourseRequest request) {
		Course course = Course.builder().title(request.getTitle()).thumbnail(request.getThumbnail())
				.description(request.getDescription()).price(request.getPrice()).curriculum(request.getCurriculum())
				.duration(request.getDuration()).category(request.getCategory()).build();
		course.setCreatedAt(LocalDateTime.now());
		course = courseRepository.save(course);
		return mapToResponse(course);
	}

	private CourseResponse mapToResponse(Course course) {
		CourseResponse.CourseResponseBuilder builder = CourseResponse.builder().id(course.getId())
				.title(course.getTitle()).thumbnail(course.getThumbnail()).description(course.getDescription())
				.price(course.getPrice()).curriculum(course.getCurriculum()).duration(course.getDuration())
				.category(course.getCategory()).createdAt(course.getCreatedAt().toString());

		if (course.getUpdatedAt() != null) {
			builder.updatedAt(course.getUpdatedAt().toString());
		}

		return builder.build();
	}


	@Override
	public List<CourseResponse> getAllCourses() {
		return courseRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public CourseResponse getCourseById(Long id) throws ResourceNotFoundException {
		Optional<Course> courseOpt = courseRepository.findById(id);
		return courseOpt.map(this::mapToResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
	}

	@Override
	public CourseResponse updateCourse(Long id, CourseRequest request) throws ResourceNotFoundException {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

		course.setTitle(request.getTitle());
		course.setThumbnail(request.getThumbnail());
		course.setDescription(request.getDescription());
		course.setPrice(request.getPrice());
		course.setCurriculum(request.getCurriculum());
		course.setDuration(request.getDuration());
		course.setCategory(request.getCategory());
		course.setUpdatedAt(LocalDateTime.now());

		Course updated = courseRepository.save(course);
		return mapToResponse(updated);
	}

	@Override
	public void deleteCourse(Long id) throws ResourceNotFoundException {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
		courseRepository.delete(course);
	}

}
