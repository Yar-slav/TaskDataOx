package com.yfedyna.taskdataox.service;

import com.yfedyna.taskdataox.dto.JobFunction;
import com.yfedyna.taskdataox.model.Job;
import java.util.List;

public interface JobScraperService {
    List<Job> scrapeJobsByFunction(JobFunction jobFunctions);
}
