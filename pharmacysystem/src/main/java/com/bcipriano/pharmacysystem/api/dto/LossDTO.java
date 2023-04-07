package com.bcipriano.pharmacysystem.api.dto;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossDTO {

    private Long id;

    private String registerDate;

    private Integer units;

    private Long lotId;

    private Lot lot;

    private Long employeeId;

    private Employee employee;

    private String description;

    public static LossDTO create(Loss loss) {
        ModelMapper modelMapper = new ModelMapper();
        LossDTO dto = modelMapper.map(loss, LossDTO.class);
        dto.lotId = loss.getLot().getId();
        dto.employeeId = loss.getEmployee().getId();
        return dto;
    }
}
