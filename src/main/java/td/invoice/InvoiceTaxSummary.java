package td.invoice;

public class InvoiceTaxSummary {
    private final Integer id;
    private final String ht;
    private final String tva;
    private final String ttc;

    public InvoiceTaxSummary(Integer id, String ht, String tva, String ttc) {
        this.id = id;
        this.ht = ht;
        this.tva = tva;
        this.ttc = ttc;
    }

    @Override
    public String toString() {
        return "InvoiceTaxSummary{" + "id=" + id + ", " + ht + ", " + tva + ", " + ttc + '}';
    }
}
