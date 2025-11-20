package com.lebuitienthuan.sequenceapp.models;

// ============================= MODEL =============================
public class NumberWrapper implements Comparable<NumberWrapper> {
    private String originalValue; // Giá trị gốc của đầu vào (ví dụ: "3/4", "4.5", "100")
    private double numericValue; // Giá trị số sau khi chuyển đổi

    public NumberWrapper(String value) throws NumberFormatException {
        this.originalValue = value.trim();
        this.numericValue = parseValue(this.originalValue);
    }

    private double parseValue(String value) throws NumberFormatException {
        try {
            // Kiểm tra nếu là phân số (chứa dấu '/')
            if (value.contains("/")) {
                String[] parts = value.split("/");
                if (parts.length != 2) {
                    throw new NumberFormatException("Invalid fraction format: " + value);
                }
                double numerator = Double.parseDouble(parts[0]);
                double denominator = Double.parseDouble(parts[1]);
                if (denominator == 0) {
                    throw new ArithmeticException("Denominator cannot be zero");
                }
                return numerator / denominator;
            }
            // Nếu không phải phân số, thử parse trực tiếp (số nguyên hoặc thập phân)
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format: " + value);
        }
    }

    public double getNumericValue() {
        return numericValue;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public int compareTo(NumberWrapper other) {
        // So sánh dựa trên giá trị số thực
        return Double.compare(this.numericValue, other.numericValue);
    }

    @Override
    public String toString() {
        // Hiển thị giá trị gốc
        return originalValue;
    }
}
