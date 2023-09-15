package com.example.rohesa.batch.jadwal.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.rohesa.model.Jadwal;
import com.example.rohesa.repository.JadwalRepository;
import java.util.List;

@Component
@StepScope
public class JadwalItemWriter implements ItemWriter<Jadwal> {
    private static final Logger logger = LogManager.getLogger(JadwalItemWriter.class);

    @Autowired
    JadwalRepository jadwalRepository;

    @Override
    public void write(List<? extends Jadwal> list) throws Exception {
        for (Jadwal data : list) {
            logger.info("Masuk Jadwal Item Writter");
            logger.info("JadwalItemWritter : Writting data : " + data.getId() + " : " + data.getStatus());
            jadwalRepository.save(data);
            logger.info("Masuk Jadwal Item Writter");
        }
    }
}
