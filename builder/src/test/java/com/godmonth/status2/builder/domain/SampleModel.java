package com.godmonth.status2.builder.domain;

import com.godmonth.status2.annotations.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Setter
@Getter
@ToString
@Entity
public class SampleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Status(triggerClass = SampleTrigger.class)
    @Enumerated(EnumType.STRING)
    private SampleStatus status;

    private String type;

    @Version
    private Long dataVersion;
}
