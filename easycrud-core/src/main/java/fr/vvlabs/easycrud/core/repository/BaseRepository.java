package fr.vvlabs.easycrud.core.repository;

import fr.vvlabs.easycrud.core.entity.EasyEntity;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T extends EasyEntity, ID extends Serializable>
    extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> {

}
