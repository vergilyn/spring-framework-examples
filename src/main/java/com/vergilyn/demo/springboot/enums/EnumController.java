package com.vergilyn.demo.springboot.enums;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.vergilyn.demo.bean.database.Parent;
import com.vergilyn.demo.constant.enums.SongsEnum;
import com.vergilyn.demo.springboot.enums.bean.SongsBean;
import com.vergilyn.demo.springboot.enums.bean.SongsDto;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 枚举返回页面。(基于枚举的下拉框选项、各种常量类型的设计思路之一,便于维护。
 * 但更优的还是应该是数据库维护。枚举还是要通过修改代码来新增加类型等)
 *
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/9
 */
@Controller
public class EnumController {
    static final SongsEnum[] songs = SongsEnum.values();

    @GetMapping("/enum")
    public ModelAndView enumIndex() {
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("beans", enumsBean());
        model.put("dtos", enumsDto());
        model.put("jsons", enumsJson());

        return new ModelAndView("enums/enum_index", model);
    }


    private static List<SongsBean> enumsBean() {
        List<SongsBean> list = new ArrayList<SongsBean>();
        for (SongsEnum song : songs) {
            list.add(new SongsBean(song));
        }
        return list;
    }

    private static List<SongsDto> enumsDto(){
        List<SongsDto> list = new ArrayList<SongsDto>();
        for (SongsEnum song : songs) {
            list.add(new SongsDto(song));
        }
        return list;
    }

    private static String enumsJson() {
        List<SongsDto> dtos = enumsDto();

        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(SongsEnum.class);

        return JSON.toJSONString(dtos, config);
    }
}
