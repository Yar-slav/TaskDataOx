package com.yfedyna.taskdataox.service;

import com.yfedyna.taskdataox.model.Job;
import com.yfedyna.taskdataox.repository.JobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public List<Job> saveJobs(List<Job> jobs) {
        log.info("Saving {} jobs to the database.", jobs.size());
        return jobRepository.saveAll(jobs);
    }
}
