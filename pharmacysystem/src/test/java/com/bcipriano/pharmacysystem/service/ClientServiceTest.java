package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ClientServiceTest {

    @SpyBean
    ClientService clientService;

    @MockBean
    ClientRepository clientRepository;

    @Test
    public void validateClientTest() {

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
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo nome é obrigatório para o cliente.");
        client.setName("AnyString");

        // Exception if CPF is NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CPF é obrigatório para o CPF");
        client.setCpf("");

        // Exception if CPF inválid
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo CPF do cliente não é válido.");
        client.setCpf("000.000.000-00");

        // Exception if BornDate is NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo data de nascimento do cliente é obrigatório.");

        // Exception if BornDate after 1930
        client.setBornDate(LocalDate.of(1900, 01, 01));
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("A data de nascimento do cliente não é válida.");
        client.setBornDate(LocalDate.of(2000, 01,01));

        // Exception if phone iss NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone do cliente é obrigatório.");

        // Exception if phone not matches with pattern
        client.setPhone("");
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo telefone do cliente não é válido.");
        client.setPhone("(00)0000-0000");

        // Exception if email is NULL
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O campo e-mail do cliente é obrigatório.");
        client.setEmail("AnyString");

        // Exception if email not matches with pattern
        exception = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("O e-mail do cliente não é inválido.");

        // Do nothing case email is valid
        client.setEmail("email@email.com");
        Throwable exceptionFinal = Assertions.catchThrowable(()-> clientService.validateClient(client));
        Assertions.assertThat(exceptionFinal).isNull();

    }

    @Test
    public void saveClientExceptionTest(){

        Mockito.when(clientRepository.existsByCpf(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> clientService.saveClient(Client.builder()
                .address(Address.builder().build())
                .build()));

        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class);

        Mockito.verify(clientRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void updateClientExceptionTest() {

        Mockito.when(clientRepository.existsByCpf(Mockito.anyString())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> clientService.updateClient(Client.builder()
                .address(Address.builder().build())
                .build()));

        Assertions.assertThat(exception).isInstanceOf(NotFoundException.class);

        Mockito.verify(clientRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    public void getClientsByIdExceptionTest() {

        Mockito.when(clientRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> clientService.getClientById(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(NotFoundException.class);

    }

    @Test
    public void deleteClientByIdExceptionTest() {

        Mockito.when(clientRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(()-> clientService.deleteClient(Mockito.anyLong()));

        Assertions.assertThat(exception).isInstanceOf(NotFoundException.class);

        Mockito.verify(clientRepository, Mockito.never()).deleteById(Mockito.any());

    }

}
