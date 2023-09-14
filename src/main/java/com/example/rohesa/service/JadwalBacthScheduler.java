package com.example.rohesa.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.example.rohesa.model.Jadwal;
import com.example.rohesa.repository.JadwalRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import java.util.concurrent.ScheduledFuture;
import java.time.Instant;
import javax.annotation.PostConstruct;

public class JadwalBacthScheduler {
    private static final Logger logger = LogManager.getLogger(JadwalBacthScheduler.class);

    @Autowired
    @Qualifier("JadwalJob")
    private Job jadwalJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JadwalRepository jadwalRepository;

    private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

    private ThreadPoolTaskScheduler poolScheduler;

    public JadwalBacthScheduler() {
        poolScheduler = new ThreadPoolTaskScheduler();
        poolScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        poolScheduler.setPoolSize(1);
        poolScheduler.initialize();
    }

    @PostConstruct
    public void init() {
        restoredScheduledTasks();
    }

    public void restoredScheduledTasks() {
        try {
            logger.info("Init Ulang Scheduler");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateNow = now.format(formatter);
            logger.info("Date Now: " + dateNow);

            List<Jadwal> jadwals1 = jadwalRepository.getGreaterThanDate(dateNow);
            if (!jadwals1.isEmpty()) {
                logger.info("Daftar ulang schedule (Jadwal)");
                jadwals1.stream().forEach(jadwal -> {
                    String pattern = "yyyy-MM-dd HH:mm";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String execDate = simpleDateFormat.format(jadwal.getExecutionAt());

                    DateTimeFormatter ftr = DateTimeFormatter.ofPattern(pattern);
                    LocalDateTime expiredAt = LocalDateTime.parse(execDate, ftr);

                    String taskId = "task_" + String.valueOf(jadwal.getId());

                    logger.info("Jadwal Id: " + jadwal.getId());
                    logger.info("Jadwal Date: " + LocalDateTime.now());
                    logger.info("Jadwal Task Id: " + taskId);

                    JobParameters jobParameters = new JobParametersBuilder()
                            .addString("date", LocalDateTime.now().toString())
                            .addLong("id", jadwal.getId())
                            .addString("uid", jadwal.getUid())
                            .toJobParameters();

                    logger.info("Jadwal UID: " + jadwal.getUid());
                    logger.info("Jadwal Expired At: " + execDate);
                    scheduleAt(expiredAt, jobParameters, taskId);
                });
            }

            List<Jadwal> jadwals2 = jadwalRepository.getLessThanDate(dateNow);
            if (!jadwals2.isEmpty()) {
                logger.info("Jalankan job (Jadwal)");
                jadwals2.stream().forEach(jadwal -> {
                    logger.info("Jadwal Id: " + jadwal.getId());
                    logger.info("Jadwal Date: " + LocalDateTime.now());

                    JobParameters jobParameters = new JobParametersBuilder()
                            .addString("date", LocalDateTime.now().toString())
                            .addLong("id", jadwal.getId())
                            .addString("uid", jadwal.getUid())
                            .toJobParameters();

                    logger.info("Disbursement UID: " + jadwal.getUid());
                    runJob(jobParameters);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Instant scheduleAt(LocalDateTime time, JobParameters jobParameters, String taskId) {
        LocalDateTime now = LocalDateTime.now();
        if (time.isBefore(now)) {
            throw new IllegalArgumentException("You can not schedule this task as starting date/time is in the past");
        }

        ZoneId zone = ZoneId.of("Asia/Jakarta");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(time);
        Instant whenToRun = time.toInstant(zoneOffSet);
        ScheduledFuture<?> future = poolScheduler.schedule(() -> runJob(jobParameters), whenToRun);
        taskFutures.put(taskId, future);
        return whenToRun;
    }

    public void runJob(JobParameters jobParameters) {
        logger.info("Executing Job Scheduler");
        try {
            jobLauncher.run(jadwalJob, jobParameters);
        } catch (Exception e) {
            logger.info("Job Scheduler Error : " + e.getMessage());
        }
    }

    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
    }
}
