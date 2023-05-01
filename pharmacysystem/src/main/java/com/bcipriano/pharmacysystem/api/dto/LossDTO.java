package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.validation.constraints.DateFormat;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Register date", example = "2025-01-01 (YYYY-MM-DD)", required = true)
    @DateFormat(message = "Data de registro inválida.")
    private String registerDate;

    @ApiModelProperty(value = "Units", example = "15 (Integer)", required = true)
    @Min(value = 0, message = "Valor de unidades inválido.")
    private Integer units;

    @ApiModelProperty(value = "Lot ID", example = "3 (Long)", required = true)
    @NotNull(message = "Lot ID inválido.")
    private Long lotId;

    @ApiModelProperty(hidden = true)
    private String lotNumber;

    @ApiModelProperty(value = "Employee ID", example = "2 (Long)", required = true)
    @NotNull(message = "Funcionário ID inválido.")
    private Long employeeId;

    @ApiModelProperty(hidden = true)
    private String employeeName;

    @ApiModelProperty(value = "Description", example = "Damaged packaging", required = true)
    @NotBlank(message = "Insira uma descrição")
    private String description;

    public static LossDTO create(Loss loss) {
        ModelMapper modelMapper = new ModelMapper();
        LossDTO dto = modelMapper.map(loss, LossDTO.class);
        dto.lotId = loss.getLot().getId();
        dto.employeeId = loss.getEmployee().getId();
        dto.employeeName = loss.getEmployee().getName();
        dto.lotNumber = loss.getLot().getNumber();
        return dto;
    }
}