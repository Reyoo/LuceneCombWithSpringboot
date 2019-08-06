package com.nko.example.lucene.model;

import lombok.Data;
import lombok.ToString;

/**
 * @ProjectName: lucene
 * @Package: com.nko.example.lucene.model
 * @ClassName: Student
 * @Author: sun71
 * @Description: model
 * @Date: 2019/8/1 10:28
 * @Version: 1.0
 */
@Data
public class Student {

    public Student(String name,String studentId,String sex){
        this.name = name;
        this.studentId = studentId;
        this.sex = sex;
    }
    String name;
    String studentId;
    String sex;

}
