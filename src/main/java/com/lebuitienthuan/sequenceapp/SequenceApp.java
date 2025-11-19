package com.lebuitienthuan.sequenceapp;

import com.lebuitienthuan.sequenceapp.controllers.SequenceController;
import com.lebuitienthuan.sequenceapp.models.SequenceModel;
import com.lebuitienthuan.sequenceapp.services.SequenceService;
import com.lebuitienthuan.sequenceapp.view.SequenceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SequenceApp {
    public static void main(String[] args) {
        // Khởi tạo các thành phần MVC
        SequenceModel model = new SequenceModel();
        SequenceView view = new SequenceView();
        SequenceService service = new SequenceService();
        SequenceController controller = new SequenceController(model, view, service);

        controller.startApplication();
    }
}
