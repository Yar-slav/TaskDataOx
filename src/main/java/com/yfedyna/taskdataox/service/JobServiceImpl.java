package com.yfedyna.taskdataox.service;

import com.yfedyna.taskdataox.model.Job;
import com.yfedyna.taskdataox.repository.JobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public List<Job> saveJobs(List<Job> jobs) {
        return jobRepository.saveAll(jobs);
    }
}
