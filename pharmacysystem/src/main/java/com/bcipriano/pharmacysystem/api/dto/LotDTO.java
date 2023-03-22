package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotDTO {
     private Long id;

     private String number;

     private LocalDate expirationDate;

     private Long purchaseId;

     private Long merchandiseId;

     private String merchandiseName;

     private Integer units;

     public static LotDTO create(Lot lot) {
          ModelMapper modelMapper = new ModelMapper();
          LotDTO dto = modelMapper.map(lot, LotDTO.class);
          dto.purchaseId = lot.getPurchase().getId();
          dto.merchandiseId = lot.getMerchandise().getId();
          dto.merchandiseName = lot.getMerchandise().getName();
          return dto;
     }

}
