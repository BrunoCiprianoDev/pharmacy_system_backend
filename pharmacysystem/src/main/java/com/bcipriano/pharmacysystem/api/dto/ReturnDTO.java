package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.validation.constraints.DateFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Register date", example = "2025-01-01 (YYYY-MM-DD)", required = true)
    @DateFormat(message = "Data de registro inválida.")
    private String registerDate;

    @ApiModelProperty(value = "Description", example = "Damaged packaging", required = true)
    @NotBlank(message = "Insira uma descrição")
    private String description;

    @NotNull(message = "Invalid sale item id")
    private Long saleItemId;

    private SaleItem saleItem;

    public static ReturnDTO create(Return returnObj){
        ModelMapper modelMapper = new ModelMapper();
        ReturnDTO dto = modelMapper.map(returnObj, ReturnDTO.class);
        dto.saleItemId = returnObj.getSaleItem().getId();
        return dto;
    }
}