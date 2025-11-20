package com.lebuitienthuan.sequenceapp.controllers;

import com.lebuitienthuan.sequenceapp.models.NumberWrapper;
import com.lebuitienthuan.sequenceapp.models.SequenceModel;
import com.lebuitienthuan.sequenceapp.services.SequenceService;
import com.lebuitienthuan.sequenceapp.view.SequenceView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.Collections;
// ============================= CONTROLLER =============================
public class  SequenceController {
    private SequenceModel model;
    private SequenceView view;
    private SequenceService service;
    private boolean running = true;

    public SequenceController(SequenceModel model, SequenceView view, SequenceService service) {
        this.model = model;
        this.view = view;
        this.service = service;
    }

    public void startApplication() {
        view.displayWelcome();
        // Bắt đầu bằng việc yêu cầu nhập chuỗi
        processInputSequence();
        
        while (running) {
            view.displayCurrentSequence(model.getSequence());
            view.displayMenu();
            String choice = view.getInput("").toLowerCase();
            
            view.displaySeparator();
            
            switch (choice) {
                case "1":
                    processInputSequence();
                    break;
                case "2":
                    processFunctionSelection();
                    break;
                case "3":
                    processContinueOptions();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    view.displayError("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    break;
            }
        }
        view.displayMessage("Cảm ơn bạn đã sử dụng. Chương trình kết thúc.");
    }

    private void processInputSequence() {
        String inputMethod = "";
        while (!inputMethod.equals("a") && !inputMethod.equals("b")) {
            view.displayInputMethods();
            inputMethod = view.getInput("").toLowerCase();
            if (inputMethod.equals("c")) { // Hỗ trợ thoát khỏi vòng lặp nếu người dùng muốn hủy
                return;
            }
        }

        List<NumberWrapper> newSequence = new ArrayList<>();
        boolean inputSuccessful = false;

        if (inputMethod.equals("a")) {
            // Nhập chuỗi trên một dòng
            String line = view.getInput("Nhập chuỗi số (ví dụ: 3, 4.5, 3/4, 2, 5, 100): ");
            // Tách bằng dấu phẩy, loại bỏ khoảng trắng dư thừa
            String[] tokens = line.split(",");
            for (String token : tokens) {
                if (token.trim().isEmpty()) continue;
                try {
                    newSequence.add(new NumberWrapper(token));
                } catch (Exception e) {
                    view.displayError("Lỗi định dạng số: " + token.trim() + ". Vui lòng nhập lại toàn bộ chuỗi.");
                    newSequence.clear(); // Xóa tất cả đầu vào lỗi
                    return;
                }
            }
            inputSuccessful = !newSequence.isEmpty();
        } else if (inputMethod.equals("b")) {
            // Nhập từng phần tử
            view.displayMessage("Bắt đầu nhập từng phần tử. Nhập 'DONE' để kết thúc.");
            String token;
            int count = 1;
            while (!(token = view.getInput("Phần tử thứ " + (count++) + ": ")).equalsIgnoreCase("DONE")) {
                if (token.isEmpty()) continue;
                try {
                    newSequence.add(new NumberWrapper(token));
                } catch (Exception e) {
                    view.displayError("Lỗi định dạng số: " + token + ". Vui lòng nhập lại phần tử này.");
                    count--; // Giảm số lượng để nhập lại
                }
            }
            inputSuccessful = !newSequence.isEmpty();
        }

        if (inputSuccessful) {
            model.setSequence(newSequence);
            view.displayMessage("Đã nhập thành công chuỗi mới!");
        } else if (model.isEmpty()) {
            view.displayError("Chuỗi trống. Vui lòng nhập ít nhất một phần tử.");
        }
    }

    private void processFunctionSelection() {
        if (model.isEmpty()) {
            view.displayError("Chuỗi trống. Vui lòng nhập chuỗi trước khi chọn chức năng (Lựa chọn 1).");
            return;
        }

        view.displayProcessFunctions();
        String choice = view.getInput("").toLowerCase();

        switch (choice) {
            case "a": // Max
                NumberWrapper max = service.findMax(model.getSequence());
                view.displayResult("Giá trị lớn nhất", max != null ? max.getOriginalValue() : "Không tìm thấy");
                break;
            case "b": // Min
                NumberWrapper min = service.findMin(model.getSequence());
                view.displayResult("Giá trị nhỏ nhất", min != null ? min.getOriginalValue() : "Không tìm thấy");
                break;
            case "c": // Sort
                processSorting();
                break;
            case "d": // Search
                processSearching();
                break;
            default:
                view.displayError("Lựa chọn chức năng không hợp lệ.");
                break;
        }
    }

    private void processSorting() {
        List<NumberWrapper> currentSequence = model.getSequence();
        
        // 1. KIỂM TRA ĐIỀU KIỆN SẮP XẾP THÔNG MINH
        int sortStatus = service.checkSortedStatus(currentSequence);
        List<NumberWrapper> sequenceForSort = new ArrayList<>(currentSequence); 
        
        if (sortStatus != 0) {
            String statusType = (sortStatus == 1) ? "Tăng dần" : "Giảm dần";
            
            view.displayMessage("\n--- Phát hiện Sắp xếp Thông minh (Smart Sort Detection) ---");
            view.displayMessage("✅ Chuỗi đã được sắp xếp sẵn (" + statusType + "). Bỏ qua thuật toán.");
            
            // Nếu chuỗi đã giảm dần, ta cần đảo ngược để có kết quả TĂNG DẦN cuối cùng
            if (sortStatus == -1) { 
                Collections.reverse(sequenceForSort);
                view.displayMessage("Đã thực hiện Đảo ngược để có kết quả Tăng dần.");
            }
            
            // Cập nhật Model và hiển thị kết quả cuối cùng
            model.setSequence(sequenceForSort);
            model.setSorted(true);
            
            // Sử dụng formatSequence từ service
            String finalResultStr = service.formatSequence(sequenceForSort); 
            view.displayResult("Chuỗi ĐÃ SẮP XẾP Tăng dần (Tự động)", finalResultStr);
            return; // Dừng lại, không cần chạy Bubble/Insertion Sort
        }

        // --- NẾU CHƯA SẮP XẾP, TIẾP TỤC CHẠY THUẬT TOÁN NHƯ BÌNH THƯỜNG ---
        view.displaySortingMethods();
        String choice = view.getInput("").toLowerCase();
        
        List<String> steps;
        String sortType;

        switch (choice) {
            case "1": // Bubble Sort
                sortType = "Bubble Sort";
                steps = service.bubbleSort(sequenceForSort);
                break;
            case "2": // Insertion Sort
                sortType = "Insertion Sort";
                steps = service.insertionSort(sequenceForSort);
                break;
            default:
                view.displayError("Lựa chọn thuật toán sắp xếp không hợp lệ.");
                return;
        }
        
        // 1. Hiển thị quy trình sắp xếp
        view.displaySortingProcess(sortType, steps);

        // 2. Cập nhật Model với chuỗi đã sắp xếp và trạng thái
        model.setSequence(sequenceForSort); 
        model.setSorted(true);
        view.displayMessage("Đã sắp xếp chuỗi bằng " + sortType + "!");
        
        // 3. Hiển thị kết quả cuối cùng
        String finalResult = service.formatSequence(sequenceForSort); // Sử dụng formatSequence từ service
        view.displayResult("Chuỗi ĐÃ SẮP XẾP", finalResult);
    }
    
    private void processSearching() {
        view.displaySearchMethods();
        String choice = view.getInput("").toLowerCase();

        String targetStr = view.getInput("Nhập phần tử cần tìm kiếm: ");
        NumberWrapper target;
        try {
            target = new NumberWrapper(targetStr);
        } catch (Exception e) {
            view.displayError("Phần tử tìm kiếm không hợp lệ: " + e.getMessage());
            return;
        }
        
        List<NumberWrapper> sequence = model.getSequence();
        
        // 1. LUÔN tìm tất cả các chỉ mục trong MẢNG HIỆN TẠI ("mảng cũ")
        List<Integer> currentIndices = service.findAllIndices(sequence, target);
        String currentIndicesStr = currentIndices.stream()
                                                    .map(i -> String.valueOf(i))
                                                    .collect(Collectors.joining(", "));

        int rank = 0;
        NumberWrapper foundElement = null;

        if (currentIndices.isEmpty()) {
            view.displayResult("Kết quả Tìm kiếm", "Không tìm thấy phần tử '" + targetStr + "'.");
            return;
        }

        // Tạo bản sao và sắp xếp để tính Rank (hạng)
        List<NumberWrapper> sortedSequence = new ArrayList<>(sequence);
        sortedSequence.sort(Comparator.naturalOrder());
        rank = service.findRank(sortedSequence, target);
        foundElement = sequence.get(currentIndices.get(0));


        switch (choice) {
            case "1": // Linear Search
                view.displayMessage("Thực hiện Linear Search...");
                
                view.displayResult(
                    "Kết quả Linear Search", 
                    "Giá trị tìm thấy: " + foundElement.getOriginalValue() + 
                    "\nIndex trong chuỗi hiện tại (Bắt đầu từ 0): " + currentIndicesStr  // Hiển thị tất cả chỉ mục cũ
                );
                view.displayElementRank(targetStr, rank);
                break;

            case "2": // Binary Search (Improved: Auto-sort copy + Index cũ)
                view.displayMessage("Thực hiện Binary Search trên bản sao đã sắp xếp...");

                // Tìm chỉ mục trong MẢNG ĐÃ SẮP XẾP
                int sortedIndex = service.binarySearch(sortedSequence, target);
                
                view.displayResult(
                    "Kết quả Binary Search (Trên mảng đã sắp xếp)", 
                    "Giá trị tìm thấy : " + foundElement.getOriginalValue() + 
                    "\nIndex trong MẢNG ĐÃ SẮP XẾP (Bắt đầu từ 0): " + (sortedIndex != -1 ? sortedIndex : "Không áp dụng cho kết quả này") +
                    "\nIndex trong MẢNG GỐC (Bắt đầu từ 0): " + currentIndicesStr // Hiển thị chỉ mục cũ
                );
                view.displayElementRank(targetStr, rank);
                break;
                
            default:
                view.displayError("Lựa chọn thuật toán tìm kiếm không hợp lệ.");
        }
    }

    private void processContinueOptions() {
        view.displayContinueOptions();
        String choice = view.getInput("").toLowerCase();
        
        switch (choice) {
            case "a":
                view.displayMessage("Tiếp tục lựa chọn tính năng...");
                // Quay lại vòng lặp chính
                break;
            case "b":
                processInputSequence();
                break;
            case "c":
                running = false;
                break;
            default:
                view.displayError("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                break;
        }
    }
}
