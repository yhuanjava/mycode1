package com.yan.yygh.cmn.service.impl;

import com.yan.yygh.cmn.mapper.DictMapper;
import com.yan.yygh.cmn.service.DictService;
import com.yan.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper,Dict> implements DictService {
    @Override
    public List<Dict> findChildData(Long id) {
        //1、根据parent_id = ？ 查询数据字典列表
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(queryWrapper);

        //2、判断每个节点是否存在下级，如果存在，hasChildren=true，否则hasChildren=false
        dictList.forEach(dict -> {
            boolean bo = this.isHasChildren(dict);
            dict.setHasChildren(bo);
        });

        return dictList;
    }

    //查询当前dict是否有下级，返回bol
    private boolean isHasChildren(Dict dict) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",dict.getId());

        return baseMapper.selectCount(queryWrapper)>0;
    }
}
