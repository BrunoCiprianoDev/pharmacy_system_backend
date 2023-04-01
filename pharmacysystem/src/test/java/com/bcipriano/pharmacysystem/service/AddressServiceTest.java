package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.repository.AddressRepository;
import com.bcipriano.pharmacysystem.service.impl.AddressServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AddressServiceTest {

    @SpyBean
    AddressServiceImpl addressService;

    @MockBean
    AddressRepository addressRepository;

    @Test
    public void testValidateAddress(){

        Address address = Address.builder().build();

        // Exception if Cep is null
        Throwable exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Cep inválido.");

        // Exception if Cep not maches with the pattern xx.xxx-xxx
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Cep inválido.");
        address.setCep("00.000-000");

        // Exception if Uf is null
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("UF inválido.");
        address.setUf("");

        // Exception if UF length != 2
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("UF inválido.");
        address.setUf("LL");

        // Exception if City is null
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo cidade é obrigatório.");
        address.setCity("AnyString");

        // Exception if Neightborhood is null
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo bairro é obrigatório.");
        address.setNeightborhood("AnyString");

        // Exception if AddressDetail is null
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo logradouro é obrigatório.");
        address.setAddressDetail("AnyString");

        // Exception if Number is null
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo númbero é obrigatório.");
        address.setNumber("AnyString");

        // Exception if Complement is null
        exception = Assertions.catchThrowable(()-> AddressService.validateAddress(address));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo complemento é obrigatório.");

    }

    @Test
    public void getAddressById() {

        Mockito.when(addressRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(()-> addressService.getAddressById(Mockito.anyLong()));
        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

    }

    @Test
    public void deleteAddress() {

        Mockito.when(addressRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(()-> addressService.deleteAddress(Mockito.anyLong()));
        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

    }

}
