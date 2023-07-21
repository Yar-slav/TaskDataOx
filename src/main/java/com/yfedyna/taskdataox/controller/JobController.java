package com.yfedyna.taskdataox.controller;

import com.yfedyna.taskdataox.dto.JobFunction;
import com.yfedyna.taskdataox.model.Job;
import com.yfedyna.taskdataox.service.JobScraperService;
import com.yfedyna.taskdataox.service.JobService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobScraperService jobScraperService;
    private final JobService jobService;

    @GetMapping("/scrape")
    public List<Job> scrapeJobs(@RequestBody(required = false) JobFunction jobFunctions) {
        List<Job> jobs = jobScraperService.scrapeJobsByFunction(jobFunctions);
        return jobService.saveJobs(jobs);
    }
}
