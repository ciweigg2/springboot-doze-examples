package com.example.springbootdozeexamples.config;

import com.example.springbootdozeexamples.converter.ScoreConverter;
import com.example.springbootdozeexamples.domain.AddressDomain;
import com.example.springbootdozeexamples.domain.FieldTypeDomain;
import com.example.springbootdozeexamples.domain.StudentDomain;
import com.example.springbootdozeexamples.domain.vo.AddressVo;
import com.example.springbootdozeexamples.domain.vo.FieldTypeVo;
import com.example.springbootdozeexamples.domain.vo.StudentDifferentVo;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

/**
 * @author 马秀成
 * @date 2019/6/20
 * @jdk.version 1.8
 * @desc
 */
@Configuration
public class DozerConfig {

    @Bean
    public Mapper dozerMapper() {
        Mapper mapper = DozerBeanMapperBuilder.create()
                //指定 dozer mapping 的配置文件(放到 resources 类路径下即可)，可添加多个 xml 文件，用逗号隔开
                .withMappingFiles("dozerBeanMapping.xml")
                .withMappingBuilder(beanMappingBuilder())
                .build();
        return mapper;
    }

    @Bean
    public BeanMappingBuilder beanMappingBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                // 个性化配置添加在此

                //测试所有properties，为不同名的 property 手动配置映射关系
//                mapping(StudentDomain.class, StudentDifferentVo.class)
//                        .fields("address", "addr");

                //关闭隐式匹配 Dozer 只会为我们匹配我们显式指定的 field
//                mapping(StudentDomain.class, StudentDifferentVo.class, TypeMappingOptions.wildcard(false))
//                        .fields("address", "addr");

                //测试所有properties，为不同名的 property 手动配置映射关系，排除 mobile 字段
//                mapping(StudentDomain.class, StudentDifferentVo.class)
//                        .exclude("mobile")
//                        .fields("address", "addr");

                //对象通常嵌套对象或者集合对象，Dozer 可以递归完成相关映射
                mapping(AddressDomain.class, AddressVo.class)
                        .fields("detail", "detailAddr");

                //测试深度索引匹配 courses集合中的第一条数据匹配到StudentVo中的counsellor字段
//                mapping(StudentDomain.class, StudentDifferentVo.class)
//                        .fields("courses[0].teacherName", "counsellor");

                //上面说到多数类型 Dozer 可以默认做转换，但是 Date 和 String 不可以，我们需要指定 date-formate 格式
                mapping(FieldTypeDomain.class, FieldTypeVo.class,
                        TypeMappingOptions.dateFormat("yyyy-MM-dd")
                        //添加指定id 不然上面定义过的对象映射下面就不能定义相同的了 使用了指定id的映射其他映射都不参与
                        , TypeMappingOptions.mapId("userEntrollmentDateConverter"));

                //我们可以为 mapping 设置 mapId， 在转换的时候指定 mapId，mapId 可以设置在类级别，也可以设置在 field 级别，实现一次定义，多处使用，同时也可以设置转换方向从默认的双向变为单向（one way）
                mapping(StudentDomain.class, StudentDifferentVo.class, TypeMappingOptions.mapId("userFieldOneWay"))
                        .fields("age", "age", FieldsMappingOptions.useMapId("addrAllProperties"), FieldsMappingOptions.oneWay());

                //当有些字段需要特殊处理的时候，我们需要实现自定义转换，也就是需要自定义 Converter 假设 StudentDomain.java 有 Integer 类型的 score 字段，StudentVo.java 中表示的分数则是 Enum 类型，分为 A/B/C/D 四个等级
                mapping(StudentDomain.class, StudentDifferentVo.class)
                        .fields("score", "score", customConverter(ScoreConverter.class));
            }
        };
    }

}
