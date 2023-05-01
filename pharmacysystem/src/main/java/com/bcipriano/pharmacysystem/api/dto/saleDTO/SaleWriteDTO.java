package com.bcipriano.pharmacysystem.api.dto.saleDTO;

import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemWriteDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleWriteDTO {

    @ApiModelProperty(value = "Employee ID", example = "4", required = true)
    @NotNull(message = "Funcionário não indentificado.")
    private Long employeeId;

    @ApiModelProperty(value = "Client ID", example = "3")
    private Long clientId;

    @ApiModelProperty(value = "Sale Items list", required = true)
    @NotNull(message = "Uma venda deve conter, pelo menos, 1 item.")
    private List<SaleItemWriteDTO> saleItems;

}
