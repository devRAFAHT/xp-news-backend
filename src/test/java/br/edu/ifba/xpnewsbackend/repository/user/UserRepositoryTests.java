package br.edu.ifba.xpnewsbackend.repository.user;

import br.edu.ifba.xpnewsbackend.tests.Factory;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import br.edu.ifba.xpnewsbackend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // Anotação que indica que o teste será executado com um banco de dados em memória, configurado pelo Spring
public class UserRepositoryTests {

    @Autowired  // Injeta automaticamente a instância do repositório no teste
    private UserRepository repository;

    // Definição de variáveis para os dados de teste
    User validUser;
    User existingUser;
    Long validId;
    String validUsername;
    Long invalidId;
    String invalidUsername;
    String validEmail;
    String invalidEmail;

    @BeforeEach  // Este método será executado antes de cada teste, configurando os dados necessários
    void setUp() {
        // Criando os usuários para os testes utilizando uma fábrica de dados
        validUser = Factory.createUser();
        existingUser = Factory.createExistingUser();

        // Configura o id para null, para simular que os usuários ainda não foram persistidos
        validUser.setId(null);
        existingUser.setId(null);

        // Dados válidos e inválidos para testes
        validId = 1L;
        invalidId = 2L;
        validUsername = "rafa12";
        invalidUsername = "invalid";
        validEmail = "rafa@gmail.com";
        invalidEmail = "invalid@gmail.com";
    }

    @Test
    void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        // Verifica se o usuário é persistido corretamente com autoincremento de ID
        Long totalUsers = repository.count();  // Conta o número total de usuários no banco
        User savedUser = repository.save(validUser);  // Persiste o usuário no banco

        // Verifica se o usuário foi salvo corretamente
        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > totalUsers);  // ID deve ser maior que o total inicial (autoincremento)
        assertEquals(validUser, savedUser);  // Verifica se o usuário salvo é o mesmo
    }

    @Test
    void saveShouldThrowDataIntegrityViolationExceptionWhenUsernameIsExisting(){
        // Testa a situação em que tentamos salvar um usuário com um nome de usuário já existente
        repository.save(existingUser);  // Persiste um usuário já existente

        validUser.setUsername(existingUser.getUsername());  // Define o nome de usuário do usuário válido como já existente

        // Verifica se ocorre uma exceção de violação de integridade de dados
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            repository.save(validUser);  // Tenta salvar o usuário com nome de usuário duplicado
        });
    }

    @Test
    void saveShouldThrowDataIntegrityViolationExceptionWhenEmailIsExisting() {
        // Testa a situação em que tentamos salvar um usuário com um email já existente
        repository.save(existingUser);  // Persiste um usuário já existente

        validUser.setEmail(existingUser.getEmail());  // Define o email do usuário válido como já existente

        // Verifica se ocorre uma exceção de violação de integridade de dados
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            repository.save(validUser);  // Tenta salvar o usuário com email duplicado
        });
    }

    @Test
    void findByIdShouldReturnOptionalOfUserWhenIdIsValid(){
        // Testa se o método de busca por ID retorna um usuário válido quando o ID existe
        validUser = repository.save(validUser);  // Persiste o usuário no banco

        Optional<User> result = repository.findById(validUser.getId());  // Busca o usuário pelo ID

        // Verifica se o resultado contém o usuário correto
        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
    }

    @Test
    void findByIdShouldRetunEmptyOptionalOfUserWhenIdIsInvalid(){
        // Testa se o método de busca por ID retorna um Optional vazio quando o ID não existe
        Optional<User> result = repository.findById(999L);  // Busca um ID inexistente

        // Verifica se o resultado está vazio
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmailShouldReturnOptionalOfUserWhenEmailIsValid() {
        // Testa se o método de busca por email retorna um usuário válido quando o email existe
        validUser = repository.save(validUser);  // Persiste o usuário no banco

        Optional<User> result = repository.findByEmail(validUser.getEmail());  // Busca o usuário pelo email

        // Verifica se o resultado contém o usuário correto
        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
    }

    @Test
    void findByEmailShouldReturnEmptyOptionalOfUserWhenEmailIsInvalid() {
        // Testa se o método de busca por email retorna um Optional vazio quando o email não existe
        Optional<User> result = repository.findByEmail("invalid@example.com");  // Busca um email inexistente

        // Verifica se o resultado está vazio
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsernameShouldReturnOptionalOfUserWhenUsernameIsValid() {
        // Testa se o método de busca por nome de usuário retorna um usuário válido quando o nome de usuário existe
        validUser = repository.save(validUser);  // Persiste o usuário no banco

        Optional<User> result = repository.findByUsername(validUser.getUsername());  // Busca o usuário pelo nome de usuário

        // Verifica se o resultado contém o usuário correto
        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
    }

    @Test
    void findByUsernameShouldReturnEmptyOptionalOfUserWhenUsernameIsInvalid() {
        // Testa se o método de busca por nome de usuário retorna um Optional vazio quando o nome de usuário não existe
        Optional<User> result = repository.findByUsername("invalidUsername");  // Busca um nome de usuário inexistente

        // Verifica se o resultado está vazio
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteShouldDeleteWhenUserExisting(){
        // Testa se o método de exclusão funciona corretamente para um usuário existente
        validUser = repository.save(validUser);  // Persiste o usuário no banco

        repository.delete(validUser);  // Exclui o usuário

        Optional<User> result = repository.findById(validUser.getId());  // Tenta buscar o usuário excluído

        // Verifica se o resultado está vazio após a exclusão
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldRetunrPageOfUser(){
        // Testa se o método de busca de todos os usuários retorna uma página de usuários corretamente
        repository.save(existingUser);
        repository.save(validUser);

        Pageable pageable = PageRequest.of(0, 10);  // Define a página com 10 usuários por vez
        Page<UserProjection> result = repository.findAllPageable(pageable);  // Busca a página de usuários

        // Verifica se o resultado não está vazio e as informações da página estão corretas
        assertFalse(result.isEmpty());
        assertEquals(2, result.getContent().size());  // Verifica se o tamanho da página é 2
        assertEquals(2, result.getTotalElements());  // Verifica o total de elementos na página
        assertEquals(1, result.getTotalPages());  // Verifica o número total de páginas
    }

    @Test
    void saveShouldUpdateWhenUserIsExisting(){
        // Testa se o método de salvar atualiza um usuário existente corretamente
        String usernameOriginal = validUser.getUsername();
        validUser = repository.save(validUser);  // Persiste o usuário no banco

        validUser.setUsername("novoNome");  // Atualiza o nome de usuário
        validUser = repository.save(validUser);  // Salva a atualização

        // Verifica se o nome de usuário foi atualizado
        assertNotEquals(usernameOriginal, validUser.getUsername());
    }
}
