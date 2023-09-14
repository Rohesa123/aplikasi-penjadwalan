package com.example.rohesa.batch.jadwal.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.rohesa.model.Jadwal;
import com.example.rohesa.repository.JadwalRepository;

import io.micrometer.core.lang.NonNull;

@Component
@StepScope
public class JadwalItemProcessor implements ItemProcessor<Jadwal, Jadwal> {
    private static final Logger logger = LogManager.getLogger(JadwalItemProcessor.class);

    @Autowired
    JadwalRepository jadwalRepository;

    @Override
    public Jadwal process(@NonNull Jadwal item) {
        Jadwal jadwal = jadwalRepository.findById(item.getId()).get();
        jadwal.setId(item.getId());
        jadwal.setStatus("1");
        jadwal.setDescription(item.getReqDescription());
        jadwal.setReqDescription(null);

        return jadwal;
    }
}
