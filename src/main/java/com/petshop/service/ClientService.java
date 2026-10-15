package com.petshop.service;

import com.petshop.domain.Client;
import com.petshop.repository.ClientRepository;
import com.petshop.web.dto.ClientRequest;
import com.petshop.web.dto.ClientResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> list(String q) {
        List<Client> clients;
        if (q == null || q.isBlank()) {
            clients = clientRepository.findAllByOrderByNameAsc();
        } else {
            clients = clientRepository.search(q.trim());
        }
        return clients.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClientResponse get(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public ClientResponse create(ClientRequest request) {
        Client client = new Client();
        apply(client, request);
        clientRepository.save(client);
        return toResponse(client);
    }

    @Transactional
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = findById(id);
        apply(client, request);
        return toResponse(client);
    }

    @Transactional
    public void delete(Long id) {
        Client client = findById(id);
        clientRepository.delete(client);
    }

    private Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private void apply(Client client, ClientRequest request) {
        client.setName(request.getName().trim());
        client.setPhone(blankToNull(request.getPhone()));
        client.setMobile(blankToNull(request.getMobile()));
        String email = blankToNull(request.getEmail());
        if (email != null && !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido");
        }
        client.setEmail(email != null ? email.toLowerCase() : null);
        client.setDocument(blankToNull(request.getDocument()));
        client.setAddress(blankToNull(request.getAddress()));
        client.setNotes(blankToNull(request.getNotes()));
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getPhone(),
                client.getMobile(),
                client.getEmail(),
                client.getDocument(),
                client.getAddress(),
                client.getNotes(),
                client.getCreatedAt(),
                client.getUpdatedAt());
    }
}
