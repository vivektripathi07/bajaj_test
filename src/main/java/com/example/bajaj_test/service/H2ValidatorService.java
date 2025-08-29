package com.example.bajaj_test.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class H2ValidatorService {
    private final Logger log = LoggerFactory.getLogger(H2ValidatorService.class);

    public void validateIfAvailable(String finalQuery) {
        try {
            ClassPathResource schemaRes = new ClassPathResource("validation/schema.sql");
            if (!schemaRes.exists()) {
                log.info("No schema.sql found, skipping H2 validation.");
                return;
            }
            String schemaSql = readResource(schemaRes.getInputStream());
            ClassPathResource dataRes = new ClassPathResource("validation/data.sql");
            String dataSql = dataRes.exists() ? readResource(dataRes.getInputStream()) : null;

            try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")) {
                executeStatements(conn, schemaSql);
                if (dataSql != null) executeStatements(conn, dataSql);

                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(finalQuery)) {
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    int row = 0;
                    while (rs.next() && row < 5) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= cols; i++) {
                            sb.append(md.getColumnLabel(i)).append("=").append(rs.getObject(i)).append(" | ");
                        }
                        log.info("Row: {}", sb);
                        row++;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("H2 validation failed: {}", e.getMessage());
        }
    }

    private void executeStatements(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (String part : sql.split(";")) {
                if (!part.trim().isEmpty()) st.execute(part.trim());
            }
        }
    }

    private String readResource(InputStream in) {
        try (Scanner s = new Scanner(in, StandardCharsets.UTF_8)) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
