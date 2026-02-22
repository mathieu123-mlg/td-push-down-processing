package td.invoice;

public class InvoiceStatusTotals {
    private final Double totalPaid;
    private final Double totalConfirmed;
    private final Double totalDraft;

    public InvoiceStatusTotals(Double totalPaid, Double totalConfirmed, Double totalDraft) {
        this.totalPaid = totalPaid;
        this.totalConfirmed = totalConfirmed;
        this.totalDraft = totalDraft;
    }

    public InvoiceStatusTotals() {
        this.totalPaid = 0.0;
        this.totalConfirmed = 0.0;
        this.totalDraft = 0.0;
    }

    @Override
    public String toString() {
        return "InvoiceStatusTotals{" +
               "totalPaid=" + totalPaid +
               ", totalConfirmed=" + totalConfirmed +
               ", totalDraft=" + totalDraft +
               '}';
    }
}
