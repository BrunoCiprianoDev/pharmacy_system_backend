package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleRepository;
import com.bcipriano.pharmacysystem.service.SaleItemService;
import com.bcipriano.pharmacysystem.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {

    private SaleRepository saleRepository;
    private EmployeeRepository employeeRepository;
    private ClientRepository clientRepository;
    private LotRepository lotRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, EmployeeRepository employeeRepository, ClientRepository clientRepository, LotRepository lotRepository) {
        this.saleRepository = saleRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public void validateSale(Sale sale) {

        if (sale.getTotal() < 0) {
            throw new BusinessRuleException("Valor inválido para o total.");
        }
        if (sale.getEmployee() == null || !employeeRepository.existsById(sale.getEmployee().getId())) {
            throw new BusinessRuleException("Funcionário inválido.");
        }
        if (sale.getClient() != null) {
            if (!clientRepository.existsById(sale.getClient().getId())) {
                throw new BusinessRuleException("Cliente inválido.");
            }
        }
    }

    @Override
    public Sale saveSale(Sale sale) {
        validateSale(sale);
        sale.setSaleDate(LocalDate.now()); //Verificar comportamento no DB de produção <----
        return saleRepository.save(sale);
    }

    @Override
    public Sale updateSale(Sale sale) {
        if (!saleRepository.existsById(sale.getId())) {
            throw new BusinessRuleException("A venda que está tentando modificar não está cadastrada.");
        }
        validateSale(sale);
        return saleRepository.save(sale);
    }

    @Override
    public List<Sale> getSale() {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSaleById(Long id) {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new InvalidIdException();
        }
        return sale.get();
    }

    @Override
    public List<Sale> findSaleByQuery(String query) {
        return saleRepository.findSaleByQuery(query);
    }

    @Override
    public void deleteSale(Long id) {
        if (saleRepository.existsById(id)) {
            throw new InvalidIdException();
        }
        saleRepository.deleteById(id);
    }
}
