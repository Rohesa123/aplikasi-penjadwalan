package com.example.rohesa.batch.jadwal.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.rohesa.batch.jadwal.listener.JadwalJobResultListener;
import com.example.rohesa.batch.jadwal.processor.JadwalItemProcessor;
import com.example.rohesa.batch.jadwal.reader.JadwalItemReader;
import com.example.rohesa.batch.jadwal.writer.JadwalItemWriter;
import com.example.rohesa.model.Jadwal;
import com.example.rohesa.service.JadwalBatchService;

@Configuration
@EnableBatchProcessing
public class JadwalBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    JadwalBatchService jadwalBatchService;

    @Autowired
    JadwalItemReader jadwalItemReader;

    @Autowired
    JadwalItemWriter jadwalItemWriter;

    @Autowired
    JadwalItemProcessor jadwalItemProcessor;
    
    @Bean(name = "JadwalJob")
    public Job jadwalJob() {
        return jobBuilderFactory.get("JadwalJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JadwalJobResultListener(jadwalBatchService))
                .flow(jadwalStep()).end().build();
    }

    @Bean(name = "JadwalStep")
    public Step jadwalStep() {
        return stepBuilderFactory.get("JadwalStep")
                .<Jadwal, Jadwal>chunk(1)
                .reader(jadwalItemReader)
                .processor(jadwalItemProcessor)
                .writer(jadwalItemWriter)
                .build();
    }
}
