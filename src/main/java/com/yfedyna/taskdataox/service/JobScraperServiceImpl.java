package com.yfedyna.taskdataox.service;

import com.yfedyna.taskdataox.dto.JobFunction;
import com.yfedyna.taskdataox.model.Job;
import com.yfedyna.taskdataox.service.library.JobPageScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobScraperServiceImpl implements JobScraperService {

    private final JobPageScraper jobPageScraper;

    @Override
    public List<Job> scrapeJobsByFunction(JobFunction jobFunction, int size) {
        String siteUrl = jobPageScraper.getCurrentSiteUrl(jobFunction);
        List<Job> jobs = new ArrayList<>();

        try {
            int prevSize = 0;
            int page = 1;
            while (jobs.size() < size) {
                String pageUrl = getPageUrl(jobFunction, siteUrl, page);

                log.info("Connection to the main page {} ", page);
                Document document = Jsoup.connect(pageUrl).get();
                Elements elementsByAttributeValue = document.getElementsByAttributeValue("data-testid", "job-list-item");

                if (isNoNewDataLoaded(prevSize, elementsByAttributeValue)) break;
                prevSize = elementsByAttributeValue.size();

                for (int i = (page-1)*20; i<elementsByAttributeValue.size(); i++) {
                    Job job = getJob(jobFunction, elementsByAttributeValue, i);
                    if (job == null) continue;
                    jobs.add(job);

                    if (jobs.size() >= size) break;
                }
                page++;
            }

        } catch (IOException e) {
            log.error("An error occurred while connecting to the website or parsing data.", e);
            e.printStackTrace();
        }
        return jobs;
    }

    private static String getPageUrl(JobFunction jobFunction, String siteUrl, int page) {
        String pageUrl;
        if(jobFunction == null) {
            pageUrl = siteUrl + "?page=" + page;
        } else {
            pageUrl = siteUrl + "&page=" + page;
        }
        return pageUrl;
    }

    private static boolean isNoNewDataLoaded(int prevSize, Elements elementsByAttributeValue) {
        if (elementsByAttributeValue.size() == prevSize) {
            log.info("No new data is loaded. Exiting the loop.");
            return true;
        }
        return false;
    }

    private static Job getJob(JobFunction jobFunction, Elements elementsByAttributeValue, int i) throws IOException {
        Element element = elementsByAttributeValue.get(i);
        Job job = new Job();
        String jobPageUrl = getJobPageUrl(element);
        if (jobPageUrl == null) return null;

        job.setJobPageUrl(jobPageUrl);
        job.setPositionName(element.select(".sc-beqWaB.kToBwF").text());
        job.setOrganizationTitle(Objects.requireNonNull(element.selectFirst("a[data-testid='link']")).text());
        job.setLogoUrl(element.select("img[data-testid='image']").attr("src"));
        job.setTags(getTags(element));

        log.info("Connection to the job page {}", jobPageUrl);
        Document jobDetails = Jsoup.connect(jobPageUrl).get();
        job.setLaborFunction(getLabourFunction(jobFunction, jobDetails));
        job.setOrganizationUrl(getOrganizationUrl(jobDetails));
        job.setPostedDate(Objects.requireNonNull(jobDetails.selectFirst(".sc-beqWaB.gRXpLa")).text());
        job.setLocations(getLocations(jobDetails));
        job.setDescription(jobDetails.getElementsByAttributeValue("data-testid", "careerPage").html());
        return job;
    }

    private static String getJobPageUrl(Element element) {
        String jobTechstarsSite = "https://jobs.techstars.com/";
        String jobPageUrl = Objects.requireNonNull(element.selectFirst("a[data-testid='job-title-link']")).attr("href");
        if (!jobPageUrl.startsWith("http")) {
            return jobTechstarsSite + jobPageUrl;
        } else {
            log.warn("Job page URL is not start from '{}', skipping.", jobTechstarsSite);
            return null;
        }
    }

    private static String getOrganizationUrl(Document jobDetails) {
        Elements organizationButtons = jobDetails.select("a[data-testid='button']");
        String organizationUrl = null;
        if (organizationButtons.size() >= 2) {
            organizationUrl = organizationButtons.get(1).attr("href");
        }
        return organizationUrl;
    }


    private static String getLabourFunction(JobFunction jobFunction, Document jobDetails) {
        if (jobFunction == null) {
            return jobDetails.select(".sc-beqWaB.bpXRKw").get(4).text();
        } else {
            return jobFunction.getJobFunction();
        }
    }

    private static List<String> getLocations(Document jobDetails) {
        String location = jobDetails.select(".sc-beqWaB.bpXRKw").get(5).text();
        return Arrays.stream(location.split(";")).toList();
    }

    private static List<String> getTags(Element element) {
        Elements tagElements = element.getElementsByAttributeValue("class", "sc-dmqHEX OHsAR");
        List<String> tags = new ArrayList<>();
        for (Element tagElement : tagElements) {
            tags.add(tagElement.text());
        }
        return tags;
    }

}
