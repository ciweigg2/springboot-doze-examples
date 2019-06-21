package com.example.springbootdozeexamples.domain.vo;

import com.example.springbootdozeexamples.domain.AddressDomain;
import com.example.springbootdozeexamples.domain.CourseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 马秀成
 * @date 2019/6/20
 * @jdk.version 1.8
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentVo {

    /**
     * 身份ID
     */
    private Long id;

    /**
     * 姓名
     *
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 电话
     */
    private String mobile;


    /**
     * 地址
     */
    private AddressDomain address;

    /**
     * 课程
     */
    private List<CourseDomain> courses;

    /**
     * 入学日期
     */
    private String enrollmentDate;

    /**
     * 分数
     */
    private Integer score;

}
