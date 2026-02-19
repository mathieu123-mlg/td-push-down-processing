package td;

import java.util.Objects;

public class InvoiceTotal {
    private final Integer id;
    private final String customerName;
    private final Status status;
    private final Double total;

    public InvoiceTotal(Integer id, String customerName, Status status) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = 0.0;
    }

    public InvoiceTotal(Integer id, String customerName, Status status, Double total) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = total == null ? 0.0 : total;
    }

    public Integer getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Status getStatus() {
        return status;
    }

    public Double getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTotal that = (InvoiceTotal) o;
        return Objects.equals(id, that.id) && Objects.equals(customerName, that.customerName) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, status);
    }

    @Override
    public String toString() {
        return "InvoiceTotal{" +
               "id=" + id +
               ", customerName='" + customerName + '\'' +
               ", status=" + status +
               ", total=" + total +
               '}';
    }
}
