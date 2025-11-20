package com.lebuitienthuan.sequenceapp.view;
import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import com.lebuitienthuan.sequenceapp.models.NumberWrapper;
public class SequenceView {
    private Scanner scanner;

    public SequenceView() {
        this.scanner = new Scanner(System.in);
    }

    public void displayWelcome() {
        System.out.println("==================================================");
        System.out.println("  ỨNG DỤNG XỬ LÝ CHUỖI SỐ (SEQUENCE ANALYZER)");
        System.out.println("  Ngôn Ngữ: Java");
        System.out.println("==================================================");
    }

    public void displayMenu() {
        System.out.println("\n--- MENU CHỨC NĂNG ---");
        System.out.println("1. Nhập chuỗi mới");
        System.out.println("2. Lựa chọn chức năng xử lý chuỗi");
        System.out.println("3. Tiếp tục/dừng lại");
        System.out.println("0. Thoát chương trình");
        System.out.print("Vui lòng nhập lựa chọn (0-3): ");
    }

    public void displayInputMethods() {
        System.out.println("\n--- 1. LỰA CHỌN CÁCH NHẬP CHUỖI ---");
        System.out.println("a. Nhập chuỗi trên một dòng (cách nhau bằng dấu phẩy: 3, 4.5, 3/4, 2, 5, 100)");
        System.out.println("b. Nhập từng phần tử (Nhấn ENTER sau mỗi phần tử)");
        System.out.print("Vui lòng nhập lựa chọn (a/b): ");
    }

    public void displayProcessFunctions() {
        System.out.println("\n--- 2. CHỨC NĂNG XỬ LÝ CHUỖI ---");
        System.out.println("a. Tìm giá trị lớn nhất (Max)");
        System.out.println("b. Tìm giá trị nhỏ nhất (Min)");
        System.out.println("c. Sắp xếp chuỗi");
        System.out.println("d. Tìm kiếm phần tử (Search)");
        System.out.print("Vui lòng nhập lựa chọn (a/b/c/d): ");
    }

    public void displaySortingMethods() {
        System.out.println("\n--- 2.c. SẮP XẾP CHUỖI ---");
        System.out.println("- 1. Bubble Sort");
        System.out.println("- 2. Insertion Sort");
        System.out.print("Vui lòng nhập lựa chọn (1/2): ");
    }

    public void displaySearchMethods() {
        System.out.println("\n--- 2.d. TÌM KIẾM PHẦN TỬ ---");
        System.out.println("- 1. Linear Search (Tìm kiếm tuyến tính)");
        System.out.println("- 2. Binary Search (Tìm kiếm nhị phân )");
        System.out.print("Vui lòng nhập lựa chọn (1/2): ");
    }

    public void displayContinueOptions() {
        System.out.println("\n--- 3. LỰA CHỌN TIẾP TỤC ---");
        System.out.println("a. Tiếp tục lựa chọn tính năng (quay lại Menu chính)");
        System.out.println("b. Nhập chuỗi mới");
        System.out.println("c. Kết thúc chương trình");
        System.out.print("Vui lòng nhập lựa chọn (a/b/c): ");
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    public void displayCurrentSequence(List<NumberWrapper> sequence) {
        if (sequence == null || sequence.isEmpty()) {
            System.out.println("Chuỗi hiện tại: [Trống]");
        } else {
            String seqStr = sequence.stream()
                .map(NumberWrapper::getOriginalValue)
                .collect(Collectors.joining(", "));
            System.out.println("\nChuỗi hiện tại: [" + seqStr + "]");
        }
    }
    
    public void displayMessage(String message) {
        System.out.println(message);
    }
    
    public void displayError(String error) {
        System.out.println("Lỗi: " + error);
    }
    
    public void displayResult(String title, String result) {
        System.out.println("\n--- KẾT QUẢ " + title.toUpperCase() + " ---");
        System.out.println(result);
        System.out.println("-------------------------------------");
    }

    public void displayElementRank(String element, int rank) {
        String suffix = "";
        if (rank == 1) suffix = " (lớn nhất)";
        if (rank > 0) {
            System.out.println("Phần tử '" + element + "' là phần tử lớn thứ " + rank + suffix + " trong chuỗi.");
        } else {
            System.out.println("Phần tử '" + element + "' không tìm thấy trong chuỗi.");
        }
    }
    
    public void displaySeparator() {
        System.out.println("==================================================");
    }

    // Thêm phương thức này vào class SequenceView

    public void displaySortingProcess(String title, List<String> steps) {
        System.out.println("\n--- QUY TRÌNH " + title.toUpperCase() + " ---");
        for (String step : steps) {
            System.out.println(step);
        }
        System.out.println("-------------------------------------");
}
}
