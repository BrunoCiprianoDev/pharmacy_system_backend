package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;

import com.bcipriano.pharmacysystem.validation.constraints.DateFormat;
import com.bcipriano.pharmacysystem.validation.constraints.LotNumber;
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
public class LotDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Lot number", example = "AAA-9999", required = true)
    @LotNumber(message = "Lote inválido.")
    private String number;

    @ApiModelProperty(value = "Expiration date", example = "2025-01-01 (YYYY-MM-DD)", required = true)
    @DateFormat(message = "Data inválida.")
    private String expirationDate;

    @ApiModelProperty(value = "Purchase ID", example = "4 (Long)", required = true)
    @NotNull(message = "ID da compra inválido.")
    private Long purchaseId;

    @ApiModelProperty(value = "Merchandise ID", example = "15 (Long)", required = true)
    @NotNull(message = "ID da mercadoria inválido.")
    private Long merchandiseId;

    @ApiModelProperty(value = "Units", example = "15 (Integer)", required = true)
    @Min(value = 0, message = "Valor de unidades inválido.")
    private Integer units;

    @ApiModelProperty(hidden = true)
    private Merchandise merchandise;

    @ApiModelProperty(hidden = true)
    private String noteNumberPurchase;

    public static LotDTO create(Lot lot) {
        ModelMapper modelMapper = new ModelMapper();
        LotDTO lotDto = modelMapper.map(lot, LotDTO.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lotDto.expirationDate = lot.getExpirationDate().format(formatter);

        lotDto.purchaseId = lot.getPurchase().getId();
        lotDto.merchandiseId = lot.getMerchandise().getId();
        lotDto.noteNumberPurchase = lot.getPurchase().getNoteNumber();
        return lotDto;
    }

}