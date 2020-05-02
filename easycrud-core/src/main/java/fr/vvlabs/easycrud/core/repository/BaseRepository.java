package fr.vvlabs.easycrud.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> {

}
