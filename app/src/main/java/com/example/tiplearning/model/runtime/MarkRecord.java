package com.example.tiplearning.model.runtime;

import com.example.tiplearning.model.entity.Record;

import java.util.List;

public class MarkRecord {

    public List<Record> recordList;
    public float score;

    public MarkRecord(){

    }

    public MarkRecord(List<Record> recordList,float score){
        this.recordList = recordList;
        this.score = score;
    }

}