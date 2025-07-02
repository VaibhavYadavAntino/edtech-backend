package com.edtech.dto;

import com.edtech.enums.CourseCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {
	private String title;
	private String thumbnail;
	private String description;
	private Double price;
	private String curriculum;
	private String duration;
	private CourseCategory category;
}
