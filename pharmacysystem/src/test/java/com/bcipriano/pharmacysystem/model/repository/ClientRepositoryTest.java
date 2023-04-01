package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Client;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void testMustReturnListOfClientsByName(){

        Client clientOne = Client.builder().name("ClientOne").build();
        clientRepository.save(clientOne);

        Client clientTwo = Client.builder().cpf("ClientCpf").build();
        clientRepository.save(clientTwo);

        Client clientThree = Client.builder().phone("ClientPhone").build();
        clientRepository.save(clientThree);

        List<Client> listResponse = clientRepository.findClientsByQuery("Client");

        Assertions.assertThat(listResponse).hasSize(3);
        
    }

    @Test
    public void testMustReturnTrueOrFalseByCPFClient() {
        String cpfTest = "111.111.111.11";
        Client client = Client.builder().cpf(cpfTest).build();
        clientRepository.save(client);

        boolean result = clientRepository.existsByCpf(cpfTest);

        Assertions.assertThat(result).isTrue();


    }

}
