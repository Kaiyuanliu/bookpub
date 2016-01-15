package com.kaiyuan.bookpub.repository;


import com.kaiyuan.bookpub.domain.Publisher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long>{
}
