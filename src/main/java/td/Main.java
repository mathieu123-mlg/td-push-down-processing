package td;

public class Main {
    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();

        System.out.println(dataRetriever.findInvoiceTotals());

        System.out.println(dataRetriever.findConfirmedAndPaidInvoiceTotals());

        System.out.println(dataRetriever.computeStatusTotals());

        System.out.println(dataRetriever.computeWeightedTurnover(100.0, 50.0, 0.0));

        System.out.println(dataRetriever.findInvoiceTaxSummaries());

        System.out.println(dataRetriever.computeWeightedTurnoverTtc(100.0, 50.0, 0.0));
    }
}
