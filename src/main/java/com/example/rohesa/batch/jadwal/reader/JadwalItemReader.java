package com.example.rohesa.batch.jadwal.reader;

import org.springframework.stereotype.Component;
import com.example.rohesa.model.Jadwal;
import com.example.rohesa.repository.JadwalRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import javax.sql.DataSource;

@Component
@StepScope
public class JadwalItemReader extends JdbcCursorItemReader<Jadwal> {
    private static final Logger logger = LogManager.getLogger(JadwalItemReader.class);

    @Autowired
    JadwalRepository jadwalRepository;

    public JadwalItemReader(@Autowired DataSource primaryDataSource, @Value("#(jobParameters['id'])") Long id) {
        String sql = "SELECT * FROM jadwal a WHERE a.status = '0' and id = id " + String.valueOf(id);
        logger.info(sql);

        setDataSource(primaryDataSource);
        setSql(sql);
        setFetchSize(100);
        setRowMapper(new DisbursementDetailRowMapper());
    }

    public class DisbursementDetailRowMapper implements RowMapper<Jadwal> {
        @Override
        public Jadwal mapRow(ResultSet rs, int rowNuw) throws SQLException {
            Jadwal jadwal = new Jadwal();
            jadwal.setId(rs.getLong("id"));
            jadwal.setReqDescription(rs.getString("req_description"));
            return jadwal;
        }
    }
}
