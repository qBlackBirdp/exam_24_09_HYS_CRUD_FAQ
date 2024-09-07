package com.example.exam.exam_24_09_hys_crud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    private int id;
    private String regDate;
    private String updateDate;
    private String code;
    private String name;
    private boolean delStatus;
    private String delDate;
}