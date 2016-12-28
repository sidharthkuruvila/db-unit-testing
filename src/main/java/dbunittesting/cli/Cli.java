package dbunittesting.cli;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import dbunittesting.dao.JobDao;
import dbunittesting.dao.JobDaoImpl;
import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.DBType;
import dbunittesting.generated.tables.pojos.Jobs;
import dbunittesting.generated.tables.pojos.Tasks;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Cli {
    public static void main(String[] args) {
        DBProducer.getFactory(DBType.POSTGRES).initialize();

        JCommander jc = new JCommander();
        CreateJob createJobParams = new CreateJob();
        jc.addCommand("create_job", createJobParams);
        FetchJob fetchJobParams = new FetchJob();
        jc.addCommand("fetch_job", fetchJobParams);
        jc.parse(args);

        if(jc.getParsedCommand() == null){
            System.out.println("Failed");
        }
        switch (jc.getParsedCommand()) {
            case "create_job":
                createJob(createJobParams);
                break;
            case "fetch_job":
                fetchJob(fetchJobParams);
        }
    }

    private static void fetchJob(FetchJob fetchJobParams) {
        JobDao jobDao = new JobDaoImpl();
        UUID id = UUID.fromString(fetchJobParams.jobId);
        Jobs job = jobDao.getJobById(id);
        System.out.println(String.format("Job: %s", job.getName()));
    }

    private static void createJob(CreateJob createJobParams) {
        JobDao jobDao = new JobDaoImpl();
        Jobs job = new Jobs();
        job.setName(createJobParams.jobName);


        List<Tasks> tasks = Arrays.stream(createJobParams.tasks.split(",")).map(t -> {
            Tasks task = new Tasks();
            task.setTask(t);
            return task;
        }).collect(Collectors.toList());

        jobDao.createJob(job, tasks);
        System.out.println(String.format("Created job with id: %s", job.getId()));
    }


}

@Parameters(separators = "=", commandDescription = "Creat a job")
class CreateJob {
    @Parameter(names = "--name")
    String jobName;

    @Parameter(names = "--tasks")
    String tasks;
}

@Parameters(separators = "=", commandDescription = "Fetch a job")
class FetchJob {
    @Parameter(names = "--id")
    String jobId;

}