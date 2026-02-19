package td;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataRetriever {
    DBConnection dbConnection = new DBConnection();

    public List<InvoiceTotal> findInvoiceTotals () {
        String sql = """
                select i.id as invoice_id, i.customer_name, i.status,
                sum(il.quantity * il.unit_price) as total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                group by i.id order by i.id;""";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<InvoiceTotal> invoiceTotals = new ArrayList<>();
            while (rs.next()) {
                invoiceTotals.add(new InvoiceTotal(
                        rs.getInt("invoice_id"),
                        rs.getString("Customer_name"),
                        Status.valueOf(rs.getString("status")),
                        rs.getDouble("total")
                ));
            }
            return invoiceTotals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        String sql = """
                select i.id as invoice_id, i.customer_name, i.status,
                sum(il.quantity * il.unit_price) as total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                where status in ('CONFIRMED', 'PAID')
                group by i.id order by i.id;""";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<InvoiceTotal> invoiceTotals = new ArrayList<>();
            while (rs.next()) {
                invoiceTotals.add(new InvoiceTotal(
                        rs.getInt("invoice_id"),
                        rs.getString("Customer_name"),
                        Status.valueOf(rs.getString("status")),
                        rs.getDouble("total")
                ));
            }
            return invoiceTotals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public InvoiceStatusTotals computeStatusTotals() {
        String sql = """
                select
                    sum(case when status = 'PAID' then il.quantity * il.unit_price else 0.0 end)  as total_paid,
                    sum(case when status = 'CONFIRMED' then il.quantity * il.unit_price else 0.0 end)  as total_confirmed,
                    sum(case when status = 'DRAFT' then il.quantity * il.unit_price else 0.0 end)  as total_draft
                from invoice i
                join invoice_line il on i.id = il.invoice_id;""";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new InvoiceStatusTotals(
                        rs.getDouble("total_paid"),
                        rs.getDouble("total_confirmed"),
                        rs.getDouble("total_draft")
                );
            }
            throw new RuntimeException("erreur");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double computeWeightedTurnover(Double paid, Double confirmed, Double draft) {
        String sql = """
                select
                    sum(
                        case
                            when status = 'PAID' then (il.quantity * il.unit_price * ? / 100)
                            when status = 'CONFIRMED' then (il.quantity * il.unit_price * ? / 100)
                            when status = 'DRAFT' then (il.quantity * il.unit_price * ? / 100)
                            else 0.0
                        end
                    )::numeric(10,2) as total_weighted_turnover
                from invoice i
                join invoice_line il on i.id = il.invoice_id;""";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, Objects.requireNonNullElse(paid, 0.0));
            ps.setDouble(2, Objects.requireNonNullElse(confirmed, 0.0));
            ps.setDouble(3, Objects.requireNonNullElse(draft, 0.0));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_weighted_turnover");
            }
            throw new RuntimeException("Error to compute weighted turnover");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries () {
        String sql = """
                select i.id as invoice_id,
                       'HT ' || round(sum(il.quantity * il.unit_price), 2) as ht,
                       'TVA ' || round(sum(il.quantity * il.unit_price * (coalesce((select rate from tax_config), 0)/100)), 2) as tva,
                       'TTC ' || round(sum((il.quantity * il.unit_price) + (il.quantity * il.unit_price * coalesce((select rate from tax_config) ,0)/100)), 2) as ttc
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                group by i.id, i.status order by i.id;""";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<InvoiceTaxSummary> invoiceTaxSummaries = new ArrayList<>();
            while (rs.next()) {
                invoiceTaxSummaries.add(new InvoiceTaxSummary(
                        rs.getInt("invoice_id"),
                        rs.getString("ht"),
                        rs.getString("tva"),
                        rs.getString("ttc")
                ));
            }
            return invoiceTaxSummaries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal computeWeightedTurnoverTtc (Double paid, Double confirmed, Double draft) {
        String sql = """
                select sum(
                                case
                                    when i.status = 'PAID' then ((il.quantity * il.unit_price) + (il.quantity * il.unit_price *
                                        coalesce((select rate from tax_config), 0) / 100))
                                        * (? * 1 / 100)
                                    when i.status = 'CONFIRMED' then ((il.quantity * il.unit_price) + (il.quantity * il.unit_price *
                                        coalesce((select rate from tax_config), 0) / 100))
                                        * (? * 1 / 100)
                                    when i.status = 'DRAFT' then ((il.quantity * il.unit_price) + (il.quantity * il.unit_price *
                                        coalesce((select rate from tax_config), 0) / 100))
                                        * (? * 1 / 100)
                                    else 0.0
                        end)::decimal(10, 2) as weighted_turnover_Ttc
                from invoice i
                join invoice_line il on i.id = il.invoice_id;""";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, paid);
            ps.setDouble(2, confirmed);
            ps.setDouble(3, draft);
            ResultSet rs = ps.executeQuery();
            List<InvoiceTaxSummary> invoiceTaxSummaries = new ArrayList<>();
            if (rs.next()) {
                return rs.getBigDecimal("weighted_turnover_Ttc");
            }
            return BigDecimal.valueOf(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
