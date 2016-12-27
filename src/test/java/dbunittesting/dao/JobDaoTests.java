package dbunittesting.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import dbunittesting.config.TestConfig;
import dbunittesting.generated.tables.pojos.Jobs;
import dbunittesting.generated.tables.pojos.Tasks;
import dbunittesting.utils.TestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class JobDaoTests {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestUtils testUtils;

    @After
    public void cleanup() throws Exception {
        testUtils.deleteTable(dbunittesting.generated.tables.Jobs.JOBS);
        testUtils.deleteTable(dbunittesting.generated.tables.Tasks.TASKS);
    }

    @Test
    public void testCreatingAndFetchingAJob() throws Exception {
        JobDao jobDao = new JobDaoImpl();

        Jobs job = testUtils.readJsonFixture("fixtures/test_creating_and_fetching_a_job_input_job.json",
                new TypeReference<Jobs>() {
                });


        List<Tasks> tasks = testUtils.readJsonFixture("fixtures/test_creating_and_fetching_a_job_input_tasks.json",
                new TypeReference<List<Tasks>>() {
                });
        Assert.assertNull(job.getId());
        jobDao.createJob(job, tasks);
        Assert.assertNotNull(job.getId());
        UUID jobId = job.getId();
        Jobs fetchedJob = jobDao.getJobById(jobId);
        Assert.assertNotSame(job, fetchedJob);
        testUtils.assertJsonEquals("fixtures/test_creating_and_fetching_a_job_input_job.json", fetchedJob);
        Assert.assertEquals(job.getId(), fetchedJob.getId());

    }
}
