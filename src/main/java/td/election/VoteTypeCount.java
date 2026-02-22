package td.election;

public class VoteTypeCount {
    private final VoteType voteType;
    private final Long count;

    public VoteTypeCount(VoteType voteType, Long count) {
        this.voteType = voteType;
        this.count = count;
    }

    @Override
    public String toString() {
        return "VoteTypeCount{" +
               "voteType=" + voteType +
               ", count=" + count +
               '}';
    }
}
