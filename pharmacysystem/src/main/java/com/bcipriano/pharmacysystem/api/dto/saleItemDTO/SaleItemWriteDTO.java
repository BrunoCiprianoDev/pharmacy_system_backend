package com.bcipriano.pharmacysystem.api.dto.saleItemDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemWriteDTO {

    @ApiModelProperty(value = "Units", example = "4", required = true)
    @NotNull(message = "Unidades de item venda invalida.")
    private Integer units;

    @ApiModelProperty(value = "Lot ID", example = "2", required = true)
    @NotNull(message = "Item venda com lote inválido.")
    private Long lotId;

}
