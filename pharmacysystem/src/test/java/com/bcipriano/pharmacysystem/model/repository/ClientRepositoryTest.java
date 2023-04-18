package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Client;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void testFindClientByQuery() {

        Pageable pageable = PageRequest.of(0, 10);

        //Create context
        Client client1 = createClient("Client1", "1111.1111.1111.11", "(11)1111-1111", "emailClient1");
        Client client2 = createClient("Client2", "2222.2222.2222.22", "(22)2222-2222", "emailClient2");
        Client client3 = createClient("Client3", "3333.3333.3333.33", "(33)3333-3333", "emailClient3");

        Client client1Saved = clientRepository.save(client1);
        Client client2Saved = clientRepository.save(client2);
        Client client3Saved = clientRepository.save(client3);

        // Get the client 1
        Page<Client> result1 = clientRepository.findClientsByQuery(client1.getName(), pageable);
        Assertions.assertThat(result1).isNotEmpty();
        Assertions.assertThat(result1.getContent().get(0)).isEqualTo(client1Saved);

        // Get array clients by query
        Page<Client> result2 = clientRepository.findClientsByQuery("ient", pageable);
        Assertions.assertThat(result2.getContent()).hasSize(3);

        // Get client 2 by query equals to phone
        Page<Client> result3 = clientRepository.findClientsByQuery("(22)", pageable);
        Assertions.assertThat(result3.getContent().get(0)).isEqualTo(client2Saved);

        // Get client 3 by query equals to cpf
        Page<Client> result4 = clientRepository.findClientsByQuery("3333.33", pageable);
        Assertions.assertThat(result4.getContent().get(0)).isEqualTo(client3Saved);

        // Get clients by query email
        Page<Client> result5 = clientRepository.findClientsByQuery("email", pageable);
        Assertions.assertThat(result5.getContent()).hasSize(3);

    }

    @Test
    public void testExistClientByCpf() {

        String cpfTest = "000.000.000-00";
        Client client = Client.builder().cpf(cpfTest).build();
        clientRepository.save(client);

        boolean result = clientRepository.existsByCpf(cpfTest);
        Assertions.assertThat(result).isTrue();

        result = clientRepository.existsByCpf("111.111.111-11");
        Assertions.assertThat(result).isFalse();

    }

    @Test
    public void testFindClientById() {

        Client client = Client.builder().build();
        Client clientSaved = clientRepository.save(client);

        Optional<Client> result = clientRepository.findClientById(clientSaved.getId());
        Assertions.assertThat(result.get()).isEqualTo(clientSaved);

    }

    private Client createClient(String name, String cpf, String phone, String email) {
        return Client.builder().name(name).cpf(cpf).phone(phone).email(email).build();
    }

}
