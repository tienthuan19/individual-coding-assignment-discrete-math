package com.lebuitienthuan.sequenceapp.models;

class SequenceModel {
    private List<NumberWrapper> sequence;
    private boolean isSorted = false;

    public List<NumberWrapper> getSequence() {
        return sequence;
    }

    public void setSequence(List<NumberWrapper> sequence) {
        this.sequence = sequence;
        this.isSorted = false; // Khi chuỗi mới được nhập, nó chưa được sắp xếp
    }

    public boolean isSorted() {
        return isSorted;
    }

    public void setSorted(boolean sorted) {
        isSorted = sorted;
    }

    public boolean isEmpty() {
        return sequence == null || sequence.isEmpty();
    }

    public int size() {
        return sequence != null ? sequence.size() : 0;
    }
}
