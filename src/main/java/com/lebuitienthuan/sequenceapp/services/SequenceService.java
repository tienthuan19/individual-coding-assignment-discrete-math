package com.lebuitienthuan.sequenceapp.services;

import com.lebuitienthuan.sequenceapp.models.NumberWrapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;

public class SequenceService {
    public NumberWrapper findMax(List<NumberWrapper> sequence) {
        if (sequence == null || sequence.isEmpty()) return null;

        NumberWrapper max = sequence.get(0);
        for (NumberWrapper nw : sequence) {
            if (nw.compareTo(max) > 0) {
                max = nw;
            }
        }
        return max;
    }

    public NumberWrapper findMin(List<NumberWrapper> sequence) {
        if (sequence == null || sequence.isEmpty())
            return null;

        NumberWrapper min = sequence.get(0);
        for (NumberWrapper nw : sequence) {
            if (nw.compareTo(min) < 0) {
                min = nw;
            }
        }
        return min;
    }
    
    public List<String> bubbleSort(List<NumberWrapper> sequence) {
    List<String> steps = new ArrayList<>();
    if (sequence == null || sequence.size() < 2) {
        steps.add("Chuỗi quá ngắn, không cần sắp xếp.");
        return steps;
    }

    steps.add("Chuỗi ban đầu: [" + formatSequence(sequence) + "]");
    int n = sequence.size();
    boolean swapped;
    
    for (int i = 0; i < n - 1; i++) {
        swapped = false;
        steps.add("--- Vòng lặp ngoài lần " + (i + 1) + " (Phần tử lớn nhất thứ " + (i + 1) + " được đẩy về cuối) ---");

        for (int j = 0; j < n - i - 1; j++) {
            steps.add("  So sánh: " + sequence.get(j) + " và " + sequence.get(j + 1));
            if (sequence.get(j).compareTo(sequence.get(j + 1)) > 0) {
                // Hoán đổi
                NumberWrapper temp = sequence.get(j);
                sequence.set(j, sequence.get(j + 1));
                sequence.set(j + 1, temp);
                swapped = true;
                steps.add("  -> Hoán đổi: " + sequence.get(j) + " và " + temp + ". Chuỗi: [" + formatSequence(sequence) + "]");
            }
        }
        if (!swapped) {
            steps.add("Chuỗi đã sắp xếp. Dừng Bubble Sort.");
            break;
        }
    }
    return steps;
    }

    public List<String> insertionSort(List<NumberWrapper> sequence) {
    List<String> steps = new ArrayList<>();
    if (sequence == null || sequence.size() < 2) {
        steps.add("Chuỗi quá ngắn, không cần sắp xếp.");
        return steps;
    }

    steps.add("Chuỗi ban đầu: [" + formatSequence(sequence) + "]");
    int n = sequence.size();
    
    for (int i = 1; i < n; ++i) {
        NumberWrapper key = sequence.get(i);
        int j = i - 1;
        steps.add("--- Vòng lặp ngoài lần " + (i) + " (Lấy Key: " + key + ") ---");
        
        // Di chuyển các phần tử lớn hơn key sang phải
        while (j >= 0 && sequence.get(j).compareTo(key) > 0) {
            steps.add("  Di chuyển: " + sequence.get(j) + " từ index " + j + " sang index " + (j + 1));
            sequence.set(j + 1, sequence.get(j));
            j = j - 1;
        }
        sequence.set(j + 1, key);
        steps.add("  -> Chèn Key " + key + " vào index " + (j + 1) + ". Chuỗi: [" + formatSequence(sequence) + "]");
    }
    return steps;
    }

    public List<Integer> findAllIndices(List<NumberWrapper> sequence, NumberWrapper target) {
        List<Integer> indices = new ArrayList<>();
        if (sequence == null || sequence.isEmpty())
            return indices;

        // Tolerance nhỏ để so sánh double
        final double EPSILON = 1e-9;

        for (int i = 0; i < sequence.size(); i++) {

            if (Math.abs(sequence.get(i).getNumericValue() - target.getNumericValue()) < EPSILON) {
                indices.add(i);
            }
        }
        return indices;
    }
    
    public String formatSequence(List<NumberWrapper> sequence) {
        return sequence.stream()
                .map(NumberWrapper::getOriginalValue)
                .collect(Collectors.joining(", "));
    }
    
    public int binarySearch(List<NumberWrapper> sequence, NumberWrapper target) {
        if (sequence == null || sequence.isEmpty()) return -1;

        int low = 0;
        int high = sequence.size() - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sequence.get(mid).compareTo(target);

            if (comparison == 0) {
                return mid; 
            } else if (comparison < 0) {
                low = mid + 1; 
            } else {
                high = mid - 1; 
            }
        }
        return -1; 
    }

    public int findRank(List<NumberWrapper> sortedSequence, NumberWrapper target) {
        if (sortedSequence == null || sortedSequence.isEmpty())
            return 0;

        int index = binarySearch(sortedSequence, target);

        if (index == -1) {
            return 0; // Không tìm thấy
        }

        int firstIndex = index;
        while (firstIndex > 0 && sortedSequence.get(firstIndex - 1).compareTo(target) == 0) {
            firstIndex--;
        }

        return sortedSequence.size() - firstIndex;
    }
    
    public int checkSortedStatus(List<NumberWrapper> sequence) {
    if (sequence == null || sequence.size() <= 1) {
        return 1; // Coi là tăng dần nếu rỗng hoặc có 1 phần tử
    }

    boolean isAscending = true;
    boolean isDescending = true;
    final double EPSILON = 1e-9; // Sai số nhỏ để so sánh số thực

    for (int i = 0; i < sequence.size() - 1; i++) {
        // Lấy giá trị số để so sánh
        double val1 = sequence.get(i).getNumericValue();
        double val2 = sequence.get(i + 1).getNumericValue();
        
        // Kiểm tra điều kiện Tăng dần: val1 > val2
        if (val1 > val2 + EPSILON) { 
            isAscending = false;
        }
        
        // Kiểm tra điều kiện Giảm dần: val1 < val2
        if (val1 < val2 - EPSILON) { 
            isDescending = false;
        }

        // Nếu cả hai đều sai, thoát sớm
        if (!isAscending && !isDescending) {
            return 0; 
        }
    }

    if (isAscending) {
        return 1; // Đã sắp xếp tăng dần
    } else if (isDescending) {
        return -1; // Đã sắp xếp giảm dần
    } else {
        return 0; // Không sắp xếp
    }
}
}
