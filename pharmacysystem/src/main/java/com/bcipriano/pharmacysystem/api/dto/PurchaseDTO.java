package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Purchase;

import com.bcipriano.pharmacysystem.validation.constraints.DateFormat;
import com.bcipriano.pharmacysystem.validation.constraints.NoteNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Purchase date", example = "2023-01-01 (YYYY-MM-DD)", required = true)
    @DateFormat(message = "Data inválida.")
    private String purchaseDate;

    @ApiModelProperty(value = "Note number", example = "999.999.999-99", required = true)
    @NoteNumber(message = "Nota fiscal inválida.")
    private String noteNumber;

    @ApiModelProperty(value = "Total", example = "15757.78", required = true)
    @Min(value = 0, message = "O total deve ser maior que 0.")
    private Double total;

    @NotNull(message = "Fornecedor inválido.")
    private Long supplierId;

    @ApiModelProperty(hidden = true)
    private String supplierName;

    @ApiModelProperty(hidden = true)
    private String supplierCNPJ;

    public static PurchaseDTO create(Purchase purchase){
        ModelMapper modelMapper = new ModelMapper();
        PurchaseDTO dto = modelMapper.map(purchase, PurchaseDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dto.purchaseDate = purchase.getPurchaseDate().format(formatter);

        dto.supplierId = purchase.getSupplier().getId();
        dto.supplierName = purchase.getSupplier().getName();
        dto.supplierCNPJ = purchase.getSupplier().getCnpj();
        return dto;
    }

}