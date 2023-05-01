package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;

import com.bcipriano.pharmacysystem.validation.constraints.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchandiseDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "The merchandise name", example = "Product 1", required = true)
    @NotBlank(message = "O campo nome não foi preenchido.")
    private String name;

    @ApiModelProperty(value = "Code", example = "999999-999999-9", required = true)
    @Code(message = "Código de barras inválido.")
    private String code;

    @ApiModelProperty(value = "Department",
            example = "(MEDICAMENTO, DERMATOLOGIA, GERIATRIA, BELEZA, SUPLEMENTO, HIGIENE, INFANTIL , OUTRO)", required = true)
    @DepartmentValid(message = "Departamento inválido.")
    private String department;

    @ApiModelProperty(value = "Classification", example = "(REFERENCIA, SIMILAR, GENERICO, LIVRE)", required = true)
    private String classification;

    @ApiModelProperty(value = "Brand", example = "Merck Merdicines", required = true)
    @NotBlank(message = "Merca inválida.")
    private String brand;

    @ApiModelProperty(value = "Formule", example = "Metformin hydrochloride")
    private String formule;

    @ApiModelProperty(value = "Stripe", example = "(VERMELHA, AMARELA, PRETA, SEM_TARJA)", required = true)
    @StripeValid(message = "Tarja inválida.")
    private String stripe;

    @ApiModelProperty(value = "Storage temperature", example = "20")
    private Double storageTemperature;

    @ApiModelProperty(value = "RMS", example = "9999999999999")
    @Rms(message = "O valor para rms(Registro Ministerio da Saúde) de ter 13 digitos")
    private String rms;

    @ApiModelProperty(value = "Minimum stock", example = "120", required = true)
    @Min(value = 0, message = "O valor minimo para estoque mínimo é 0")
    @NotNull(message = "Estoque mínimo não informado")
    private Integer minimumStock;

    @ApiModelProperty(value = "Maximum stock", example = "320", required = true)
    @Min(value = 0, message = "O valor minimo para estoque máximo é 0")
    @NotNull(message = "Estoque máximo não informado")
    private Integer maximumStock;

    @ApiModelProperty(value = "Full price", example = "12.95", required = true)
    @Min(value = 0, message = "O valor mínimo para o preço é 0.00")
    @NotNull(message = "Preço não informado.")
    private Double fullPrice;

    @ApiModelProperty(value = "Comission", example = "2 (Percentage %)", required = true)
    private Double comission;

    @ApiModelProperty(value = "PMC", example = "16.99", required = true)
    @Min(value = 0, message = "O valor mínimo para o preço máximo é 0.00")
    @NotNull(message = "Preço máximo não informado.")
    private Double pmc;

    @ApiModelProperty(value = "Description", example = "Efficient antibiotic medication")
    private String description;

    @ApiModelProperty(value = "Discount group ID", example = "1 (Long)")
    private Long discountGroupId;

    private DiscountGroup discountGroup;

    public static MerchandiseDTO create(Merchandise merchandise) {

        ModelMapper modelMapper = new ModelMapper();
        MerchandiseDTO merchandiseDTO = modelMapper.map(merchandise, MerchandiseDTO.class);

        if (merchandise.getDiscountGroup() != null) {
            merchandiseDTO.discountGroupId = merchandise.getDiscountGroup().getId();
        }

        return merchandiseDTO;
    }

}