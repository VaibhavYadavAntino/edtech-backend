package com.edtech.dto;

import com.edtech.enums.CourseCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
	private Long id;
	private String title;
	private String thumbnail;
	private String description;
	private Double price;
	private String curriculum;
	private String duration;
	private CourseCategory category;
	private String instructorName;
	private String createdAt;
	private String updatedAt;
}
