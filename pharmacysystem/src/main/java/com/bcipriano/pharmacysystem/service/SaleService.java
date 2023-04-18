package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService{

    private SaleRepository saleRepository;

    private SaleItemService saleItemService;
    private EmployeeRepository employeeRepository;
    private ClientRepository clientRepository;
    private LotRepository lotRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository,
                           SaleItemService saleItemService,
                           EmployeeRepository employeeRepository,
                           ClientRepository clientRepository,
                           LotRepository lotRepository
    ) {
        this.saleRepository = saleRepository;
        this.saleItemService =saleItemService;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.lotRepository = lotRepository;
    }

    public void validateSale(Sale sale) {
        if (sale.getEmployee() == null || !employeeRepository.existsById(sale.getEmployee().getId())) {
            throw new BusinessRuleException("Funcionário inválido.");
        }
        if (sale.getClient() != null) {
            if (!clientRepository.existsById(sale.getClient().getId())) {
                throw new BusinessRuleException("Cliente inválido.");
            }
        }
    }

    @Transactional
    public void saveSale(Sale sale, List<SaleItem> saleItems) {
        validateSale(sale);
        sale.setSaleDate(LocalDate.now());

        Sale saleSaved = saleRepository.save(sale);

        for(SaleItem saleItem : saleItems) {
            saleItem.setSale(saleSaved);
            saleItemService.saveSaleItem(saleItem);
        }
    }

    public void updateSale(Sale sale) {
        if (!saleRepository.existsById(sale.getId())) {
            throw new BusinessRuleException("A venda que está tentando modificar não está cadastrada.");
        }
        validateSale(sale);
        saleRepository.save(sale);
    }

    public Page<Sale> getSale(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    public Sale getSaleById(Long id) {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new BusinessRuleException();
        }
        return sale.get();
    }

    public List<Sale> findSaleByQuery(String query) {
        return saleRepository.findSaleByQuery(query);
    }

    public void deleteSale(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        saleRepository.deleteById(id);
    }
}