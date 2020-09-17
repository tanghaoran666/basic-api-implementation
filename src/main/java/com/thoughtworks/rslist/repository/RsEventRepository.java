package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventPo,Integer> {
    @Override
    List<RsEventPo> findAll();
}
