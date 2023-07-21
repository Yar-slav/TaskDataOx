package com.yfedyna.taskdataox.service;

import com.yfedyna.taskdataox.model.Job;
import java.util.List;

public interface JobService {
    List<Job> saveJobs(List<Job> jobs);
}
