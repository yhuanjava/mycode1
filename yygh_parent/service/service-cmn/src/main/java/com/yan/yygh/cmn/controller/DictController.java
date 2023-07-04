package com.yan.yygh.cmn.controller;

import com.yan.yygh.cmn.service.DictService;
import com.yan.yygh.common.result.R;
import com.yan.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "数据字典接口文档")
@CrossOrigin
@RestController
@RequestMapping("/admin/cmn")
public class DictController {

    @Autowired
    DictService dictService;

    @GetMapping("findAll")
    public R findAll(){
        return R.ok().data("list",dictService.list());
    }

    @GetMapping("find")
    public String find(){
        return "1212";
    }
//    #查询一级数据字典列表（最底层的下级数据）
//    SELECT * FROM dict WHERE parent_id = 1
    @GetMapping("findChildData/{id}")
    public R findChildData(@PathVariable Long id){

        List<Dict> list = dictService.findChildData(id);

        return R.ok().data("list",list);
    }

}
