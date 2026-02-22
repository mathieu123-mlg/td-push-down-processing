package td.election;

public class VoteSummary {
    private final long validCount;
    private final long blankCount;
    private final long nullCount;

    public VoteSummary(long validCount, long blankCount, long nullCount) {
        this.validCount = validCount;
        this.blankCount = blankCount;
        this.nullCount = nullCount;
    }

    @Override
    public String toString() {
        return "VoteSummary{" +
               "validCount=" + validCount +
               ", blankCount=" + blankCount +
               ", nullCount=" + nullCount +
               '}';
    }
}
