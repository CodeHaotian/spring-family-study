package com.spring.data.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 父接口继承PagingAndSortingRepository对CrudRepository的扩展，以提供使用分页和排序。
 *
 * @author Haotian
 * @version 1.0.0
 * @date 2020/7/26 17:12
 **/
@NoRepositoryBean
public interface BaseRepository<T, Long> extends PagingAndSortingRepository<T, Long> {
    /**
     * 根据更新时间降序，再按照id进行升序
     *
     * @return 前三条数据
     */
    List<T> findTop3ByOrderByUpdateTimeDescIdAsc();
}