package fr.vvlabs.easycrud.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import fr.vvlabs.easycrud.test.model.Pet;
import fr.vvlabs.easycrud.test.repository.IPetRepository;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringJUnitConfig(classes = SpringContextIT.TestConfig.class)
class SpringContextIT {

  @Configuration
  @EnableTransactionManagement
  @EnableJpaRepositories(basePackageClasses = IPetRepository.class)
  @ComponentScan(basePackages = "fr.vvlabs.easycrud.test")
  static class TestConfig {

    @Bean
    DataSource dataSource() {
      DriverManagerDataSource ds = new DriverManagerDataSource();
      ds.setDriverClassName("org.h2.Driver");
      ds.setUrl("jdbc:h2:mem:easycrud;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
      ds.setUsername("sa");
      ds.setPassword("");
      return ds;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
      LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
      emf.setDataSource(dataSource);
      emf.setPackagesToScan(Pet.class.getPackage().getName());
      emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
      Map<String, Object> props = new HashMap<>();
      props.put("hibernate.hbm2ddl.auto", "create-drop");
      props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
      emf.setJpaPropertyMap(props);
      return emf;
    }

    @Bean
    PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
      JpaTransactionManager tx = new JpaTransactionManager();
      tx.setEntityManagerFactory(emf.getObject());
      return tx;
    }
  }

  @Test
  @DisplayName("Spring context boots and repository CRUD works")
  void contextStartsAndCrudWorks(IPetRepository repo) {
    Pet pet = new Pet();
    pet.setName("toto");
    Pet saved = repo.save(pet);
    assertNotNull(saved.getId());
    assertEquals(1, repo.count());
    assertEquals("toto", repo.findAll().iterator().next().getName());
  }
}
