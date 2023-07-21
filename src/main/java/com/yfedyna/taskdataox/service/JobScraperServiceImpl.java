package com.yfedyna.taskdataox.service;

import com.yfedyna.taskdataox.dto.JobFunction;
import com.yfedyna.taskdataox.model.Job;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class JobScraperServiceImpl implements JobScraperService {

    @Override
    public List<Job> scrapeJobsByFunction(JobFunction jobFunctions) {
        List<Job> jobs = new ArrayList<>();
        String url = "https://jobs.techstars.com/jobs";

        try {
            Document document = Jsoup.connect(url).get();
            Elements elementsByAttributeValue = document.getElementsByAttributeValue("data-testid", "job-list-item");
            for (Element element : elementsByAttributeValue) {
                Job job = new Job();
                String jobPageUrl = getJobPageUrl(element);
                if (jobPageUrl == null) continue;

                job.setJobPageUrl(jobPageUrl);
                job.setPositionName(element.select(".sc-beqWaB.kToBwF").text());

                job.setOrganizationTitle(Objects.requireNonNull(element.selectFirst("a[data-testid='link']")).text());
                job.setLogoUrl(element.select("img[data-testid='image']").attr("src"));
                job.setTags(getTags(element));

                Document jobDetails = Jsoup.connect(jobPageUrl).get();
                String laborFunction = jobDetails.select(".sc-beqWaB.bpXRKw").get(4).text();
                if (jobFunctions == null || isContainsInJobFunction(jobFunctions, laborFunction)) {
                    job.setLaborFunction(laborFunction);
                } else {
                    continue;
                }

                job.setOrganizationUrl(jobDetails.select("a[data-testid='button']").get(1).attr("href"));
                job.setPostedDate(Objects.requireNonNull(jobDetails.selectFirst(".sc-beqWaB.gRXpLa")).text());
                job.setLocations(getLocations(jobDetails));
                job.setDescription(jobDetails.getElementsByAttributeValue("data-testid", "careerPage").html());

                jobs.add(job);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    private static boolean isContainsInJobFunction(JobFunction jobFunction, String laborFunction) {
        for (String function : jobFunction.getJobFunctions()) {
            if (function.equalsIgnoreCase(laborFunction)) {
                return true;
            }
        }
        return false;
    }

    private static String getJobPageUrl(Element element) {
        String jobPageUrl = Objects.requireNonNull(element.selectFirst("a[data-testid='job-title-link']")).attr("href");
        if (!jobPageUrl.startsWith("http")) {
            return "https://jobs.techstars.com/" + jobPageUrl;
        }
        return null;
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
