package dbunittesting.dao;

import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.resources.DBFactory;
import dbunittesting.generated.tables.pojos.Jobs;
import dbunittesting.generated.tables.pojos.Tasks;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static dbunittesting.daofactory.DBType.POSTGRES;

/**
 * Created by sidharth on 27/12/16.
 */
public interface JobDao {

    DBFactory dbFactory = DBProducer.getFactory(POSTGRES);

    void createJob(Jobs job, List<Tasks> tasks);

    Jobs getJobById(UUID id);

    List<Tasks> getTasksByJobId(UUID jobId);
}
