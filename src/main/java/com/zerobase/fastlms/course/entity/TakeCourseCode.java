package com.zerobase.fastlms.course.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public interface TakeCourseCode {
    String STATUS_REQ = "REQ";
    String STATUS_COMPLETE = "COMPLETE";
    String STATUS_CANCEL = "CANCEL";




}
