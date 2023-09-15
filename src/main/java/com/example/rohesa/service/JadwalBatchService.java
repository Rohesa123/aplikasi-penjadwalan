package com.example.rohesa.service;

import org.springframework.transaction.annotation.Transactional;

import com.example.rohesa.model.Jadwal;
import com.example.rohesa.repository.JadwalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;

@Component
@Transactional
public class JadwalBatchService {
    private static final Logger logger = LogManager.getLogger(JadwalBatchService.class);

    @Autowired
    JadwalRepository jadwalRepository;

    @Transactional
    public void updateStatus(String uid, String status) {
        logger.info("update status : ID -> " + uid + " STATUS -> " + status);
        logger.info("Cek Query Listener");
        Optional<Jadwal> jadwalOpt = jadwalRepository.findByUid(uid);
        logger.info("Selesai Cek Query Listener");
        if (jadwalOpt.isPresent()) {
            Jadwal jadwal = jadwalOpt.get();
            if (!jadwal.getStatus().equals("4")) {
                jadwal.setStatus(status);
                if (status.equals("4")) {
                    jadwal.setAlasanGagal("Ini Keterangan Gagal");
                } else if (status.equals("1")) {
                    jadwal.setReqDescription("Ini Description");
                }
                jadwalRepository.save(jadwal);
            }
        }
    }

    public Boolean isTrue(String uid) {
        Optional<Jadwal> jadwal = jadwalRepository.findByUid(uid);
        if (jadwal.isEmpty()) return false;
        String cek = jadwal.get().getReqDescription();

        return cek != null || !cek.isBlank();
    }
}
