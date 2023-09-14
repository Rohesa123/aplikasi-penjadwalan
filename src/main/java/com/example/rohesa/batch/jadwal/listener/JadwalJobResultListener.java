package com.example.rohesa.batch.jadwal.listener;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.JobExecution;
import com.example.rohesa.service.JadwalBatchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;

public class JadwalJobResultListener implements JobExecutionListener {
    private static final Logger logger = LogManager.getLogger(JadwalJobResultListener.class);

    @Autowired
    JadwalBatchService jadwalBatchService;

    public JadwalJobResultListener(JadwalBatchService jadwalBatchService) {
        this.jadwalBatchService = jadwalBatchService;
    }

    public void beforeJob(JobExecution jobExecution) {
        String uid = jobExecution.getJobParameters().getString("uid");
        logger.info("Disbursement Batch Started with ID : " + uid);

        if (jadwalBatchService.isTrue(uid)) {
            jadwalBatchService.updateStatus(uid, "1");
        } else {
            jadwalBatchService.updateStatus(uid, "4");
        }
    }

    public void afterJob(JobExecution jobExecution) {
        String uid = jobExecution.getJobParameters().getString("uid");

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Jadwal Batch Completed with UID : " + uid);
            jadwalBatchService.updateStatus(uid, "2");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            logger.info("Jadwal Batch Failed with UID : " + uid);
            jadwalBatchService.updateStatus(uid, "3");
        }
    }
}
