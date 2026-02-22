package td.election;

public class CandidateVoteCount {
    private final String candidateName;
    private final Long validVoteCount;

    public CandidateVoteCount(String candidateName, Long validVoteCount) {
        this.candidateName = candidateName;
        this.validVoteCount = validVoteCount;
    }

    @Override
    public String toString() {
        return "CandidateVoteCount{" +
               "candidateName='" + candidateName + '\'' +
               ", validVoteCount=" + validVoteCount +
               '}';
    }
}
