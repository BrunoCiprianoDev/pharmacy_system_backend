package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossDTO {

    private Long id;

    private String registerDate;

    private Integer units;

    private Long lotId;

    private String lotNumber;

    private Long employeeId;

    private String employeeName;

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