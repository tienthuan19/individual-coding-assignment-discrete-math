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
// ============================= CONTROLLER =============================

/**
 * CONTROLLER: Điều khiển luồng của ứng dụng, xử lý đầu vào, gọi Service và cập nhật View.
 */
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

    /**
     * Xử lý lựa chọn nhập chuỗi mới.
     */
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

    /**
     * Xử lý lựa chọn chức năng (Max/Min/Sort/Search).
     */
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

    /**
     * Xử lý lựa chọn thuật toán sắp xếp.
     */
    private void processSorting() {
        view.displaySortingMethods();
        String choice = view.getInput("").toLowerCase();
        
        List<NumberWrapper> currentSequence = model.getSequence();
        List<NumberWrapper> sortedSequence = new ArrayList<>(currentSequence); // Tạo bản sao để sắp xếp

        switch (choice) {
            case "1": // Bubble Sort
                service.bubbleSort(sortedSequence);
                view.displayMessage("Đã sắp xếp chuỗi bằng Bubble Sort!");
                break;
            case "2": // Insertion Sort
                service.insertionSort(sortedSequence);
                view.displayMessage("Đã sắp xếp chuỗi bằng Insertion Sort!");
                break;
            default:
                view.displayError("Lựa chọn thuật toán sắp xếp không hợp lệ.");
                return;
        }
        
        // Cập nhật Model với chuỗi đã sắp xếp và trạng thái
        model.setSequence(sortedSequence); 
        model.setSorted(true);
        view.displayResult("Chuỗi đã sắp xếp", sortedSequence.stream().map(NumberWrapper::getOriginalValue).collect(Collectors.joining(", ")));
    }
    
    /**
     * Xử lý lựa chọn tìm kiếm.
     */
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
        int index = -1;

        switch (choice) {
            case "1": // Linear Search
                index = service.linearSearch(sequence, target);
                view.displayMessage("Thực hiện Linear Search...");
                // Sau Linear Search, cần xác định hạng bằng cách sắp xếp bản sao
                if (index != -1) {
                    List<NumberWrapper> sortedSequence = new ArrayList<>(sequence);
                    // Sắp xếp bản sao để tìm hạng chính xác
                    sortedSequence.sort(Comparator.naturalOrder());
                    int rank = service.findRank(sortedSequence, target);
                    view.displayResult("Kết quả Linear Search", "Tìm thấy tại chỉ mục: " + index + " (Giá trị thực: " + sequence.get(index).getNumericValue() + ")");
                    view.displayElementRank(targetStr, rank);
                } else {
                    view.displayResult("Kết quả Linear Search", "Không tìm thấy phần tử '" + targetStr + "'.");
                }
                break;
            case "2": // Binary Search
                if (!model.isSorted()) {
                    view.displayError("Không thể thực hiện Binary Search. Vui lòng sắp xếp chuỗi trước (Lựa chọn 2.c).");
                    return;
                }
                
                index = service.binarySearch(sequence, target);
                view.displayMessage("Thực hiện Binary Search...");
                if (index != -1) {
                    // Chuỗi đã được sắp xếp, sử dụng ngay để tìm hạng
                    int rank = service.findRank(sequence, target);
                    view.displayResult("Kết quả Binary Search", "Tìm thấy tại chỉ mục: " + index + " (Giá trị thực: " + sequence.get(index).getNumericValue() + ")");
                    view.displayElementRank(targetStr, rank);
                } else {
                    view.displayResult("Kết quả Binary Search", "Không tìm thấy phần tử '" + targetStr + "'.");
                }
                break;
            default:
                view.displayError("Lựa chọn thuật toán tìm kiếm không hợp lệ.");
        }
    }

    /**
     * Xử lý lựa chọn tiếp tục/dừng lại.
     */
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
