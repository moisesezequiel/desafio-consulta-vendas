package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.ReportMinDTO;
import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	
	
	public List<SummaryMinDTO> findSummary() {
		
		List<SummaryMinDTO> results = repository.findSummary();
		
		return results;
	}
	
	
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}
	
	
	public Page<ReportMinDTO> findReportByParans(String minDate, String maxDate, String name, Pageable pageable) {

		if (validateParans(minDate, maxDate, name)) {

			LocalDate inicio = LocalDate.of(Year.now().getValue(), 1, 1);
			LocalDate fim = LocalDate.of(Year.now().getValue(), 12, 31);

			Page<Sale> pedidosDoAno = repository.findByAnoAtual(inicio, fim, pageable);

			return pedidosDoAno.map(x -> new ReportMinDTO(x));

		} else {
			Page<Sale> sellers = repository.searchReportByParans(checkMinDate(minDate), checkMaxDate(maxDate), name,
					pageable);
			return sellers.map(x -> new ReportMinDTO(x));
		}

	}
    
	public boolean validateParans(String minDate, String maxDate, String name) {

		if (minDate.isBlank() || minDate.isEmpty() && maxDate.isBlank() || maxDate.isEmpty() && name.isBlank()
				|| name.isEmpty()) {
			return true;
		} else {
			return false;
		}

	}

	private LocalDate checkMaxDate(String maxDate) {

		if (maxDate.isBlank() || maxDate.isEmpty()) {

			LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
			today.minusYears(1L);

			return today;
		} else {

			LocalDate parseMaxDate = LocalDate.parse(maxDate);

			return parseMaxDate;

		}

	}

	private LocalDate checkMinDate(String minDate) {

		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		LocalDate parseMinDate = (minDate == null || minDate.isBlank()) ? today : LocalDate.parse(minDate);

		return parseMinDate;

	}
}
