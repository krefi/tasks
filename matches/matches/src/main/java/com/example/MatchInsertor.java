package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatchInsertor {

    // hardcoded variables for easier local testing
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";
    private static final String FILE_PATH = "fo_random.txt";
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws Exception {
        // read the file and create a list for every matchId
        Map<String, List<Match>> matchesByMatchId = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            // first line is header
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 3) {
                    continue;
                }

                String matchId = parts[0].replace("'", "").trim();
                int marketId = Integer.parseInt(parts[1].replace("'", "").trim());
                String outcomeId = parts[2].replace("'", "").trim();
                String specifiers = (parts.length > 3) ? parts[3].replace("'", "").trim() : null;

                matchesByMatchId.computeIfAbsent(matchId, k -> new ArrayList<>())
                        .add(new Match(matchId, marketId, outcomeId, specifiers));
            }
        }

        // prepare threads for batch inserts by MATCH_ID
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<?>> futures = new ArrayList<>();

        // create table of matches. In real world this statment wouldn't be in Java code but I put it here for simplicity
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = conn.createStatement();
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS match (
                    match_id TEXT,
                    market_id INTEGER,
                    outcome_id TEXT,
                    specifiers TEXT,
                    date_insert TIMESTAMP DEFAULT now()
                )
            """);
        }

        // start parallel inserts
        for (List<Match> matches : matchesByMatchId.values()) {
            futures.add(executor.submit(() -> insertBatch(matches)));
        }

        // finish all executions
        for (Future<?> future : futures) {
            future.get();
        }

        // print result
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT MIN(date_insert), MAX(date_insert) FROM match");
            if (rs.next()) {
                System.out.println("Min date_insert: " + rs.getTimestamp(1));
                System.out.println("Max date_insert: " + rs.getTimestamp(2));
            }
        }
    }

    /**
     * inserts list matches into match table
     *
     * @param matches list of matches
     */
    private static void insertBatch(List<Match> matches) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insertSQL = """
                INSERT INTO match (match_id, market_id, outcome_id, specifiers)
                VALUES (?, ?, ?, ?)
            """;

            // insert batch consisting of all records for a match_id
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (Match m : matches) {
                    pstmt.setString(1, m.getMatchId());
                    pstmt.setInt(2, m.getMarketId());
                    pstmt.setString(3, m.getOutcomeId());
                    pstmt.setString(4, m.getSpecifiers());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert batch for match: " + matches.get(0).getMatchId());
        }
    }
}
