package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.service.impl.ClientServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ClientServiceTest {

    @SpyBean
    ClientServiceImpl clientService;

    @MockBean
    ClientRepository clientRepository;

    @Test
    public void testValidateClient(){

        Client client = Client.builder().build();
        client.setAddress(Address.builder()
                        .cep("00.000-000")
                        .uf("XX")
                        .city("AnyString")
                        .neightborhood("AnyString")
                        .addressDetail("AnyString")
                        .number("AnyString")
                        .complement("AnyString")
                .build());

        // Exception if name is NULL
        Throwable exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome é obrigatório.");
        client.setName("AnyString");

        // Exception if CPF is NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CPF é obrigatório.");
        client.setCpf("");

        // Exception if CPF inválid
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("CPF inválido.");
        client.setCpf("000.000.000-00");

        // Exception if BornDate is NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo data de nascimento é obrigatório.");

        // Exception if BornDate after 1930
        client.setBornDate(LocalDate.of(1900, 01, 01));
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Data de nascimento inválida.");
        client.setBornDate(LocalDate.of(2000, 01,01));

        // Exception if phone iss NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone é obrigatório.");

        // Exception if phone not matches with pattern
        client.setPhone("");
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Telefone inválido.");
        client.setPhone("(00)0000-0000");

        // Exception if email is NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo e-mail é obrigatório.");
        client.setEmail("AnyString");

        // Exception if email not matches with pattern
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("E-mail inválido.");

        // Do nothing case email is valid
        client.setEmail("email@email.com");
        Throwable exceptionFinal = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exceptionFinal).isNull();

    }

    @Test
    public void testExceptionUpdateClient(){

        Mockito.when(clientRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Mockito.doNothing().when(clientService).validateClient(Mockito.any());

        Throwable exception = Assertions.catchThrowable(()-> clientService.updateClient(Client.builder().address(Address.builder().build()).build()));

        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

        Mockito.verify(clientRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void testExceptionGetClientById(){

        Mockito.when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(()-> clientService.getClientById(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

    }

    @Test
    public void testExceptionDeleteClient() {

        Mockito.when(clientRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> clientService.deleteClient(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(InvalidIdException.class);

        Mockito.verify(clientRepository, Mockito.never()).deleteById(Mockito.anyLong());

    }

}
