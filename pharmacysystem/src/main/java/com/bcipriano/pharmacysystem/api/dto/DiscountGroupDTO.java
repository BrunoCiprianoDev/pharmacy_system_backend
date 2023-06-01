package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.validation.constraints.DateFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountGroupDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Discount group name", example = "Medicine discount", required = true)
    @NotBlank(message = "O campo nome não foi preenchido.")
    private String name;

    @ApiModelProperty(value = "Start date", example = "2023-03-29 (YYYY_MM_DD)", required = true)
    @DateFormat(message = "A data de inicio inválida.")
    private String startDate;

    @ApiModelProperty(value = "Final date", example = "2023-06-29 (YYYY_MM_DD)", required = true)
    @DateFormat(message = "A data final inválida.")
    private String finalDate;

    @ApiModelProperty(value = "Percentage", example = "10.5", required = true)
    @Min(value = 1, message = "O percentual deve ser maior que 0%.")
    @Max(value = 100, message = "O percentual dever ser menor ou igual a 100%.")
    @NotNull(message = "Percentual de desconto não informado.")
    private Double percentage;

    @ApiModelProperty(value = "Minimum units", example = "3", required = true)
    @Min(value = 0, message = "O valor para unidades mínimas deve ser maior que 0.")
    @NotNull(message = "Valor para unidades mínimas não informado.")
    private Integer minimumUnits;

    @ApiModelProperty(value = "Description", example = "Discount for medications...", required = true)
    @NotBlank(message = "Insira uma descrição.")
    private String description;

    public static DiscountGroupDTO create(DiscountGroup discountGroup) {

        ModelMapper modelMapper = new ModelMapper();
        DiscountGroupDTO discountGroupDTO = modelMapper.map(discountGroup, DiscountGroupDTO.class);
        return discountGroupDTO;
    }

}
