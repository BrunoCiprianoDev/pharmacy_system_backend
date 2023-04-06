package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotDTO {
     private Long id;

     private String number;

     private String expirationDate;

     private Long purchaseId;

     private Purchase purchase;

     private Long merchandiseId;

     private Merchandise merchandise;

     private Integer units;

     public static LotDTO create(Lot lot) {
          ModelMapper modelMapper = new ModelMapper();
          LotDTO lotDto = modelMapper.map(lot, LotDTO.class);

          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          lotDto.expirationDate = lot.getExpirationDate().format(formatter);

          lotDto.purchaseId = lot.getPurchase().getId();
          lotDto.merchandiseId = lot.getMerchandise().getId();
          return lotDto;
     }

}
