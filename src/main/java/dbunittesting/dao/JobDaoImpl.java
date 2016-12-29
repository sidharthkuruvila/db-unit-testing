package dbunittesting.dao;

import dbunittesting.generated.tables.pojos.Jobs;
import dbunittesting.generated.tables.pojos.Tasks;
import dbunittesting.generated.tables.records.JobsRecord;
import dbunittesting.generated.tables.records.TasksRecord;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static dbunittesting.generated.tables.Jobs.JOBS;

/**
 * Created by sidharth on 27/12/16.
 */
public class JobDaoImpl implements JobDao {
    @Override
    public void createJob(Jobs job, List<Tasks> tasks) {
        try {
            TransactionTemplate transactionTemplate = dbFactory.getTransactionTemplate();
            try (Connection connection = dbFactory.getConnection()) {
                transactionTemplate.execute((status) -> {

                    DSLContext DB = DSL.using(connection, SQLDialect.POSTGRES_9_4);
                    UUID jobId = UUID.randomUUID();
                    JobsRecord jobsRecord = DB.newRecord(JOBS);
                    jobsRecord.setId(jobId);
                    job.setId(jobId);
                    jobsRecord.setName(job.getName());
                    tasks.stream().forEach(task -> task.setJobId(job.getId()));
                    List<TasksRecord> tasksRecords = tasks.stream().map(task -> {
                        TasksRecord tasksRecord = new TasksRecord();
                        UUID taskId = UUID.randomUUID();
                        tasksRecord.setId(taskId);
                        task.setId(taskId);
                        tasksRecord.setJobId(task.getJobId());
                        tasksRecord.setTask(task.getTask());
                        return tasksRecord;
                    }).collect(Collectors.toList());
                    DB.executeInsert(jobsRecord);
                    DB.batchInsert(tasksRecords).execute();

                    return null;
                });
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Jobs getJobById(UUID id) {
        try (Connection connection = dbFactory.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES_9_4);
            Jobs jobs = create.select().from(JOBS)
                    .where(JOBS.ID.equal(id)).fetchOneInto(Jobs.class);
            return jobs;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Tasks> getTasksByJobId(UUID jobId) {
        return null;
    }

}
