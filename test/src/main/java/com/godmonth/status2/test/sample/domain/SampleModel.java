package com.godmonth.status2.test.sample.domain;

import com.godmonth.status2.annotations.Status;
import com.godmonth.status2.test.sample.machine.trigger.SampleTrigger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

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
