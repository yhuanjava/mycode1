package com.yan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yan.service.HospitalSetService;
import com.yan.yygh.common.result.R;
import com.yan.yygh.model.hosp.HospitalSet;
import com.yan.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.jsqlparser.statement.select.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;

@Api(description = "医院设置接口")
@RestController
@CrossOrigin     //允许跨域访问
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询所有医院设置
    @ApiOperation(value = "医院设置列表")
    @GetMapping("/findAll")
    public R findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("list",list);
    }

    @ApiOperation("医院设置删除功能")
    @DeleteMapping("/{id}")
    public R removeById(@ApiParam(name = "id" ,value = "医生ID",required = true) @PathVariable Long id){
        hospitalSetService.removeById(id);
        return R.ok();
    }

    @PostMapping("{page}/{limit}")
    public R pageQuery(@PathVariable Integer page,
                       @PathVariable Integer limit,
                       @RequestBody HospitalSetQueryVo hospitalSetQueryVo){

        //1、分页对象
        Page<HospitalSet> hospitalSetPage = new Page<>(page, limit);

        //构建查询条件
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();

        if (!StringUtils.isEmpty(hoscode)){
            queryWrapper.eq("hoscode",hoscode);
        }
        if (!StringUtils.isEmpty(hosname)){
            queryWrapper.eq("hosname",hosname);
        }
        hospitalSetService.page(hospitalSetPage,queryWrapper);

        //返回状态值
        List<HospitalSet> records = hospitalSetPage.getRecords();
        long total = hospitalSetPage.getTotal();
        return R.ok().data("row",records).data("totol",total);
    }

    @ApiOperation(value = "根据id查询医院设置")
    @GetMapping("/getHospSet/{id}")
    public R getById(@ApiParam(name = "id",value = "医院id",required = true) @PathVariable String id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return R.ok().data("hospitalSet",hospitalSet);
    }

    @ApiOperation(value = "根据id更新数据")
    @PostMapping("/updateHospSet")
    public R updateById(@ApiParam(name = "id",value = "医院设置对象",required = true) @PathVariable HospitalSet hospitalSet){
        boolean updateById = hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/batchRemove")
    public R batchRemoveHospitalSet(@RequestBody List<Long> idList){
        hospitalSetService.removeByIds(idList);
        return R.ok();
    }


    @ApiOperation(value = "锁定和解锁")
    @GetMapping("lockHospitalSet/{id}/{status}")
    public R lockHospitalSet(@PathVariable Long id,
                             @PathVariable Integer status){
        //1、检查status的值是否合法
        if (status!=0||status!=1){
            return R.error().message("状态值只能为0或者1");
        }
        //2、根据id查询医院设置对象
        //原则：获取（查询）一个数据，之后使用这个数据之前，进行判空的校验
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //3、判断医院设置对象是否存在
        if (hospitalSet==null){
            return R.error().message("该医院设置不存在");
        }
        //4、判断是否重复操作 ==判断两个数字是否相等，如果Integer类型，建议equals去比较，基本数据类型使用==比较
        if (hospitalSet.getStatus().intValue()==status.intValue()){
            return R.error().message(status==0?"请勿重复锁定":"请勿重复解锁");
        }
        //5、实现更新status
        hospitalSet.setStatus(status);
        //hospitalSet.setUpdateTime(null);
        hospitalSet.setUpdateTime(new Date());
        boolean b = hospitalSetService.updateById(hospitalSet);
        return b? R.ok(): R.error();
    }
}
