package td.election;

public class ElectionResult {
    private final String candidateName;
    private final Long validVoteLong;

    public ElectionResult(String candidateName, Long validVoteLong) {
        this.candidateName = candidateName;
        this.validVoteLong = validVoteLong;
    }

    @Override
    public String toString() {
        return "ElectionResult{" +
               "candidateName='" + candidateName + '\'' +
               ", validVoteLong=" + validVoteLong +
               '}';
    }
}
