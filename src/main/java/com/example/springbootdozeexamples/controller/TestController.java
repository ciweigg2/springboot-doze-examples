package com.example.springbootdozeexamples.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springbootdozeexamples.domain.AddressDomain;
import com.example.springbootdozeexamples.domain.CourseDomain;
import com.example.springbootdozeexamples.domain.FieldTypeDomain;
import com.example.springbootdozeexamples.domain.StudentDomain;
import com.example.springbootdozeexamples.domain.vo.FieldTypeVo;
import com.example.springbootdozeexamples.domain.vo.StudentDifferentVo;
import com.example.springbootdozeexamples.domain.vo.StudentVo;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 马秀成
 * @date 2019/6/20
 * @jdk.version 1.8
 * @desc
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private Mapper dozerMapper;

    /**
     * 测试最基本的bean对象复制 相同的key
     */
    @RequestMapping(value = "/testDefault")
    public String testDefault(){
        StudentDomain studentDomain = getStudentDomain();
        StudentVo studentVo = dozerMapper.map(studentDomain, StudentVo.class);
        log.info("StudentVo: [{}]", studentVo.toString());
        studentVo.setAge(16);
        log.info("StudentDomain: [{}]", dozerMapper.map(studentVo, StudentDomain.class));
        return JSONObject.toJSONString(dozerMapper.map(studentVo, StudentDomain.class));
    }

    /**
     * 测试相同对象中不同key 需要对应映射
     */
    @RequestMapping(value = "/testDifferentAddress")
    public String testDifferentAddress(){
        StudentDomain studentDomain = getStudentDomain();
        StudentDifferentVo studentDifferentVo = dozerMapper.map(studentDomain, StudentDifferentVo.class);
        log.info("StudentDifferentVo: [{}]", studentDifferentVo.toString());
        return JSONObject.toJSONString(studentDifferentVo);
    }

    /**
     * Dozer 会隐式递归匹配所有 field，甚至集合
     */
    @RequestMapping(value = "testCascadeObject")
    public String testCascadeObject(){
        StudentDomain studentDomain = getStudentDomain();
        StudentDifferentVo studentDifferentVo = dozerMapper.map(studentDomain, StudentDifferentVo.class);
        log.info("StudentDifferentVo: [{}]", studentDifferentVo.toString());
        return JSONObject.toJSONString(studentDifferentVo);
    }

    /**
     * 测试字段类型不同的情况呀 Dozer 开箱即用的功能之一就是类型转换，多数类型我们不需要手动转换类型，完全交给 Dozer即可
     */
    @RequestMapping(value = "/testDifferentFieldType")
    public String testDifferentFieldType(){
        FieldTypeDomain fieldTypeDomain = new FieldTypeDomain();
        fieldTypeDomain.setId("123");
        fieldTypeDomain.setEnrollmentDate("2019-09-01 10:00:00");
        //使用指定映射
        FieldTypeVo fieldTypeVo = dozerMapper.map(fieldTypeDomain, FieldTypeVo.class ,"userEntrollmentDateConverter");
        log.info(fieldTypeVo.toString());
        return JSONObject.toJSONString(fieldTypeVo);
    }

    /**
     * 我们可以为 mapping 设置 mapId， 在转换的时候指定 mapId，mapId 可以设置在类级别，也可以设置在 field 级别，实现一次定义，多处使用，同时也可以设置转换方向从默认的双向变为单向（one way）
     */
    @RequestMapping(value = "/testFieldOneWay")
    public String testFieldOneWay(){
        StudentDomain studentDomain = getStudentDomain();
        StudentVo studentVo = dozerMapper.map(studentDomain, StudentVo.class, "userFieldOneWay");
        return JSONObject.toJSONString(studentVo);
    }

    private StudentDomain getStudentDomain(){
        StudentDomain studentDomain = new StudentDomain();
        studentDomain.setId(1024L);
        studentDomain.setName("tan日拱一兵");
        studentDomain.setAge(18);
        studentDomain.setMobile("13996996996");
        studentDomain.setScore(90);

        AddressDomain addressDomain = new AddressDomain();
        addressDomain.setProvince("北京");
        addressDomain.setCity("北京");
        addressDomain.setDistrict("海淀区");
        addressDomain.setDetail("西二旗");
        studentDomain.setAddress(addressDomain);

        List<CourseDomain> courseDomains = new ArrayList<>(2);
        CourseDomain englishCourse = new CourseDomain();
        englishCourse.setCourseId(1);
        englishCourse.setCourseCode("English");
        englishCourse.setCourseName("英语");
        englishCourse.setTeacherName("京晶");
        courseDomains.add(englishCourse);

        CourseDomain chineseCourse = new CourseDomain();
        chineseCourse.setCourseId(2);
        chineseCourse.setCourseCode("Chinese");
        chineseCourse.setCourseName("语文");
        chineseCourse.setTeacherName("水寒");
        courseDomains.add(chineseCourse);

        studentDomain.setCourses(courseDomains);
        studentDomain.setEnrollmentDate("2019-09-01 10:00:00");

        return studentDomain;
    }

    private AddressDomain getAddressDomain(){
        AddressDomain addressDomain = new AddressDomain();
        addressDomain.setProvince("北京");
        addressDomain.setCity("北京");
        addressDomain.setDistrict("海淀区");
        addressDomain.setDetail("西二旗");
        return addressDomain;
    }

    private CourseDomain getCourseDomain(){
        CourseDomain chineseCourse = new CourseDomain();
        chineseCourse.setCourseId(2);
        chineseCourse.setCourseCode("Chinese");
        chineseCourse.setCourseName("语文");
        chineseCourse.setTeacherName("水寒");
        return chineseCourse;
    }

}
