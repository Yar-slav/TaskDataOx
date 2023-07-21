package com.yfedyna.taskdataox.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobPageUrl;
    private String positionName;
    private String organizationUrl;
    private String logoUrl;
    private String organizationTitle;
    private String laborFunction;

    @ElementCollection
    private List<String> locations; // TODO: 7/20/23 create Location entity(country, city...)
    private String postedDate;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> tags;

}
