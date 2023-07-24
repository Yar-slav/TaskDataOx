package com.yfedyna.taskdataox.controller;

import com.yfedyna.taskdataox.dto.JobFunction;
import com.yfedyna.taskdataox.model.Job;
import com.yfedyna.taskdataox.service.JobScraperService;
import com.yfedyna.taskdataox.service.JobService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobScraperService jobScraperService;
    private final JobService jobService;

    @PostMapping("/scrape")
    public List<Job> scrapeJobs(
            @RequestBody(required = false) JobFunction jobFunction,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Received request to scrape jobs with JobFunction: {}", jobFunction);
        List<Job> jobs = jobScraperService.scrapeJobsByFunction(jobFunction, size);
        return jobService.saveJobs(jobs);
    }
}
