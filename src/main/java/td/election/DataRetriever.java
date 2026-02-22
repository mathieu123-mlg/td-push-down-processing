package td.election;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    DBConnection dbConnection = new DBConnection();

    public long countAllVotes() {
        String sql = "select count(id) from vote";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new RuntimeException("Vote is empty");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<VoteTypeCount> countVotesByType() {
        String sql = "select vote_type, count(voter_id) as count from vote group by vote_type";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<VoteTypeCount> voteTypeCounts = new ArrayList<>();
            while (rs.next()) {
                voteTypeCounts.add(new VoteTypeCount(
                        VoteType.valueOf(rs.getString("vote_type")),
                        rs.getLong("count")
                ));
            }
            return voteTypeCounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CandidateVoteCount> countValidVotesByCandidate() {
        String sql = """
                select candidate.name as candidate_name,
                       count(case when vote.vote_type = 'VALID' then voter_id end) as valid_vote
                from vote
                join candidate on candidate.id = candidate_id
                group by candidate.name;""";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<CandidateVoteCount> candidateVoteCounts = new ArrayList<>();
            while (rs.next()) {
                candidateVoteCounts.add(new CandidateVoteCount(
                        rs.getString("candidate_name"),
                        rs.getLong("valid_vote")
                ));
            }
            return candidateVoteCounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public VoteSummary computeVoteSummary() {
        String sql = """
                select count(case when vote.vote_type = 'VALID' then voter_id end) as valid_count,
                       count(case when vote.vote_type = 'BLANK' then voter_id end) as blank_count,
                       count(case when vote.vote_type = 'NULL' then voter_id end) as null_count
                from vote;""";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new VoteSummary(
                        rs.getLong("valid_count"),
                        rs.getLong("blank_count"),
                        rs.getLong("null_count")
                );
            }
            throw new RuntimeException("Vote is empty");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double computeTurnoutRate() {
        String sql = """
                select (select (select count(voter_id) from vote) /
                               (select count(id) from voter)
                        )::numeric(10, 2) as turnout_rate""";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("turnout_rate");
            }
            throw new RuntimeException("Error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ElectionResult findWinner() {
        String sql = """
                select candidate.name as candidate_name,
                       count(case when vote.vote_type = 'VALID' then voter_id end) as valid_vote_count
                from vote
                join candidate on candidate.id = candidate_id
                group by candidate.name
                order by valid_vote_count desc limit 1;""";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ElectionResult(
                        rs.getString("candidate_name"),
                        rs.getLong("valid_vote_count")
                );
            }
            throw new RuntimeException("Error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
