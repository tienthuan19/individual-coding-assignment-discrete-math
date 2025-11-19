package com.lebuitienthuan.sequenceapp.services;

// ============================= SERVICE =============================

/**
 * SERVICE: Chứa các logic nghiệp vụ (thuật toán Max/Min, Sắp xếp, Tìm kiếm).
 */
import java.util.List;
import java.util.Comparator;
import com.lebuitienthuan.sequenceapp.models.NumberWrapper;
public class SequenceService {

    /**
     * Tìm giá trị lớn nhất.
     */
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

    /**
     * Tìm giá trị nhỏ nhất.
     */
    public NumberWrapper findMin(List<NumberWrapper> sequence) {
        if (sequence == null || sequence.isEmpty()) return null;

        NumberWrapper min = sequence.get(0);
        for (NumberWrapper nw : sequence) {
            if (nw.compareTo(min) < 0) {
                min = nw;
            }
        }
        return min;
    }

    /**
     * Thuật toán Sắp xếp nổi bọt (Bubble Sort).
     * Sắp xếp tại chỗ (in-place) và trả về một tham chiếu đến chuỗi đã được sắp xếp.
     */
    public List<NumberWrapper> bubbleSort(List<NumberWrapper> sequence) {
        if (sequence == null || sequence.size() < 2) return sequence;

        int n = sequence.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // So sánh phần tử thứ j và j+1
                if (sequence.get(j).compareTo(sequence.get(j + 1)) > 0) {
                    // Hoán đổi
                    NumberWrapper temp = sequence.get(j);
                    sequence.set(j, sequence.get(j + 1));
                    sequence.set(j + 1, temp);
                }
            }
        }
        return sequence;
    }

    /**
     * Thuật toán Sắp xếp chèn (Insertion Sort).
     * Sắp xếp tại chỗ (in-place) và trả về một tham chiếu đến chuỗi đã được sắp xếp.
     */
    public List<NumberWrapper> insertionSort(List<NumberWrapper> sequence) {
        if (sequence == null || sequence.size() < 2) return sequence;

        int n = sequence.size();
        for (int i = 1; i < n; ++i) {
            NumberWrapper key = sequence.get(i);
            int j = i - 1;

            // Di chuyển các phần tử lớn hơn key sang phải
            while (j >= 0 && sequence.get(j).compareTo(key) > 0) {
                sequence.set(j + 1, sequence.get(j));
                j = j - 1;
            }
            sequence.set(j + 1, key);
        }
        return sequence;
    }

    /**
     * Thuật toán Tìm kiếm tuyến tính (Linear Search).
     * Tìm vị trí phần tử trong chuỗi (không yêu cầu sắp xếp).
     */
    public int linearSearch(List<NumberWrapper> sequence, NumberWrapper target) {
        if (sequence == null || sequence.isEmpty()) return -1;
        
        for (int i = 0; i < sequence.size(); i++) {
            // So sánh giá trị số
            if (sequence.get(i).getNumericValue() == target.getNumericValue()) {
                return i; // Trả về chỉ mục đầu tiên tìm thấy
            }
        }
        return -1; // Không tìm thấy
    }

    /**
     * Thuật toán Tìm kiếm nhị phân (Binary Search).
     * Yêu cầu chuỗi phải được sắp xếp.
     * Tìm vị trí phần tử trong chuỗi.
     */
    public int binarySearch(List<NumberWrapper> sequence, NumberWrapper target) {
        if (sequence == null || sequence.isEmpty()) return -1;

        int low = 0;
        int high = sequence.size() - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sequence.get(mid).compareTo(target);

            if (comparison == 0) {
                return mid; // Tìm thấy
            } else if (comparison < 0) {
                low = mid + 1; // Tìm ở nửa sau
            } else {
                high = mid - 1; // Tìm ở nửa trước
            }
        }
        return -1; // Không tìm thấy
    }
    
    /**
     * Tìm hạng (thứ tự lớn thứ N) của một phần tử.
     * Sử dụng chuỗi đã được sắp xếp TĂNG DẦN để xác định hạng.
     * Hạng được tính là vị trí từ cuối lên (lớn thứ 1, lớn thứ 2,...).
     * * @param sortedSequence Chuỗi đã được sắp xếp TĂNG DẦN.
     * @param target Phần tử cần tìm hạng.
     * @return Hạng của phần tử (lớn thứ N), 0 nếu không tìm thấy.
     */
    public int findRank(List<NumberWrapper> sortedSequence, NumberWrapper target) {
        if (sortedSequence == null || sortedSequence.isEmpty()) return 0;
        
        // Tìm vị trí của phần tử trong chuỗi đã sắp xếp
        int index = binarySearch(sortedSequence, target);
        
        if (index == -1) {
            return 0; // Không tìm thấy
        }
        
        // Loại bỏ các phần tử trùng lặp: tìm chỉ mục của phần tử đầu tiên trong nhóm trùng lặp
        int firstIndex = index;
        while (firstIndex > 0 && sortedSequence.get(firstIndex - 1).compareTo(target) == 0) {
            firstIndex--;
        }
        
        // Hạng (Rank) là vị trí từ cuối lên, tính từ 1.
        // Vị trí lớn nhất (Rank 1) là n-1. Vị trí tiếp theo là n-2, ...
        // Index càng lớn thì Rank càng nhỏ (Rank 1 là phần tử cuối cùng).
        // Index của phần tử đầu tiên trùng lặp là firstIndex.
        // Rank = size - firstIndex
        return sortedSequence.size() - firstIndex;
    }
}
