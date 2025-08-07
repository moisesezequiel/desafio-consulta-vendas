package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

//	
	
    @Query("SELECT obj FROM Sale obj WHERE obj.date BETWEEN :mindate AND :maxDate AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%',:name, '%'))")
    Page<Sale> searchReportByParans(LocalDate mindate,LocalDate maxDate,String name, Pageable pageable);
    

    
    @Query("SELECT p FROM Sale p WHERE p.date BETWEEN :inicio AND :fim")
    Page<Sale> findByAnoAtual(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim , Pageable pageable);
    
    
    
	@Query("select new com.devsuperior.dsmeta.dto.SummaryMinDTO(obj.seller.name , SUM(obj.amount)) "
			+ "from Sale obj "
			+ "GROUP BY obj.seller.name")
	List<SummaryMinDTO> findSummary();
	
}
