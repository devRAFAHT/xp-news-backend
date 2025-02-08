package br.edu.ifba.xpnewsbackend.service.user;

import br.edu.ifba.xpnewsbackend.infrastructure.exception.DatabaseException;
import br.edu.ifba.xpnewsbackend.infrastructure.exception.PasswordInvalidException;
import br.edu.ifba.xpnewsbackend.infrastructure.exception.ResourceNotFoundException;
import br.edu.ifba.xpnewsbackend.infrastructure.exception.UniqueViolationException;
import br.edu.ifba.xpnewsbackend.tests.Factory;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import br.edu.ifba.xpnewsbackend.user.repository.UserRepository;
import br.edu.ifba.xpnewsbackend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository repository;  // Mock do repositório, simula o comportamento da camada de dados

    @InjectMocks
    UserService service;  // A instância do serviço, onde as dependências são injetadas automaticamente

    User validUser;
    Long validId;
    String validUsername;
    Long invalidId;
    String invalidUsername;
    String validEmail;
    String invalidEmail;

    @BeforeEach
    void setUp() {
        // Configuração dos dados para os testes, criando um usuário válido e variáveis
        validUser = Factory.createUser();
        validId = 1L;
        invalidId = 2L;
        validUsername = "rafa12";
        invalidUsername = "invalid";
        validEmail = "rafa@gmail.com";
        invalidEmail = "invalid@gmail.com";
    }

    @Test
    void createShouldReturnUserObjectWhenSaveUser() {
        // Testa o método de criação de usuário.
        // Simula o comportamento do repositório, retornando o usuário válido quando o método save é chamado.
        when(repository.save(validUser)).thenReturn(validUser);

        // Chama o serviço para salvar o usuário.
        User savedUser = service.create(validUser);

        // Verifica se o usuário foi salvo corretamente.
        assertNotNull(savedUser);  // O usuário não pode ser nulo
        assertEquals(validUser.getUsername(), savedUser.getUsername());  // O nome de usuário deve ser igual
    }

    @Test
    void createShouldThrowUniqueViolationExceptionWhenSavePersonWithExistingEmail() {
        // Testa o comportamento quando tenta salvar um usuário com email já existente.
        // Simula uma exceção de violação de integridade de dados no repositório.
        when(repository.save(validUser)).thenThrow(DataIntegrityViolationException.class);

        // Espera que uma exceção UniqueViolationException seja lançada.
        UniqueViolationException exception = assertThrows(UniqueViolationException.class, () -> {
            service.create(validUser);
        });

        // Verifica se a mensagem da exceção é a esperada e se o método save foi chamado uma vez.
        assertEquals("Já tem um usuário registrado com esse username ou email", exception.getMessage());
        verify(repository, times(1)).save(validUser);  // Verifica se o método save foi chamado uma vez
    }

    @Test
    void createShouldThrowUniqueViolationExceptionWhenSavePersonWithExistingUsername() {
        // Testa o comportamento quando tenta salvar um usuário com username já existente.
        // Simula uma exceção de violação de integridade de dados no repositório.
        when(repository.save(validUser)).thenThrow(DataIntegrityViolationException.class);

        // Espera que uma exceção UniqueViolationException seja lançada.
        UniqueViolationException exception = assertThrows(UniqueViolationException.class, () -> {
            service.create(validUser);
        });

        // Verifica se a mensagem da exceção é a esperada e se o método save foi chamado uma vez.
        assertEquals("Já tem um usuário registrado com esse username ou email", exception.getMessage());
        verify(repository, times(1)).save(validUser);  // Verifica se o método save foi chamado uma vez
    }

    @Test
    void findAllShouldReturnUserProjectionPage() {
        // Testa a busca de todos os usuários com paginação.
        // Simula o retorno de uma página de projeção de usuários.
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserProjection> expectedPage = Factory.createUserProjectionPage(pageable);
        when(repository.findAllPageable(pageable)).thenReturn(expectedPage);

        // Chama o método para buscar todos os usuários com paginação.
        Page<UserProjection> result = service.findAll(pageable);

        // Verifica se o resultado retornado é igual ao esperado.
        assertEquals(expectedPage, result);
        verify(repository, times(1)).findAllPageable(pageable);  // Verifica se o método foi chamado uma vez
        assertEquals(2, result.getContent().size());  // Verifica se o tamanho da página é correto
        assertEquals(2, result.getTotalElements());  // Verifica o total de elementos na página
        assertEquals(1, result.getTotalPages());  // Verifica o número total de páginas
    }

    @Test
    void findByIdShouldReturnObjectUserWhenValidId() {
        // Testa a busca de um usuário pelo ID válido.
        // Simula a busca de um usuário com o ID válido.
        when(repository.findById(validId)).thenReturn(Optional.of(validUser));

        // Chama o método para buscar o usuário por ID.
        User result = service.findById(validId);

        // Verifica se o usuário foi encontrado e se os dados são corretos.
        assertNotNull(result);  // O resultado não pode ser nulo
        assertEquals(validUser, result);  // O usuário retornado deve ser igual ao usuário esperado
        verify(repository, times(1)).findById(validId);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
        // Testa o comportamento quando o usuário não é encontrado pelo ID.
        // Simula uma resposta vazia quando tenta buscar um usuário com um ID inválido.
        String expectedMessage = "Nenhum usuário foi encontrado com o id: " + invalidId;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        // Espera que uma exceção ResourceNotFoundException seja lançada.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(invalidId);
        });

        // Verifica se a mensagem da exceção é a esperada e se o método foi chamado uma vez.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, times(1)).findById(invalidId);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void findByUsernameShouldReturnObjectUserWhenValidUsername() {
        // Testa a busca de um usuário pelo nome de usuário válido.
        // Simula a busca de um usuário com o nome de usuário válido.
        when(repository.findByUsername(validUsername)).thenReturn(Optional.of(validUser));

        // Chama o método para buscar o usuário pelo nome de usuário.
        User result = service.findByUsername(validUsername);

        // Verifica se o usuário foi encontrado e se os dados são corretos.
        assertNotNull(result);  // O resultado não pode ser nulo
        assertEquals(validUser.getUsername(), result.getUsername());  // O nome de usuário deve ser igual
        verify(repository, times(1)).findByUsername(validUsername);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void findByUsernameShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
        // Testa o comportamento quando o nome de usuário não é encontrado.
        // Simula uma resposta vazia quando tenta buscar um usuário com nome de usuário inválido.
        String expectedMessage = "Nenhum usuário foi encontrado com o username: " + invalidUsername;
        when(repository.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        // Espera que uma exceção ResourceNotFoundException seja lançada.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findByUsername(invalidUsername);
        });

        // Verifica se a mensagem da exceção é a esperada e se o método foi chamado uma vez.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, times(1)).findByUsername(invalidUsername);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void findByEmailShouldReturnObjectUserWhenValidEmail() {
        // Testa a busca de um usuário pelo email válido.
        // Simula a busca de um usuário com o email válido.
        when(repository.findByEmail(validEmail)).thenReturn(Optional.of(validUser));

        // Chama o método para buscar o usuário pelo email.
        User result = service.findByEmail(validEmail);

        // Verifica se o usuário foi encontrado e se os dados são corretos.
        assertNotNull(result);  // O resultado não pode ser nulo
        assertEquals(validUser.getEmail(), result.getEmail());  // O email deve ser igual
        verify(repository, times(1)).findByEmail(validEmail);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void findByEmailShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
        // Testa o comportamento quando o email não é encontrado.
        // Simula uma resposta vazia quando tenta buscar um usuário com email inválido.
        String expectedMessage = "Nenhum usuário foi encontrado com o email: " + invalidEmail;
        when(repository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        // Espera que uma exceção ResourceNotFoundException seja lançada.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findByEmail(invalidEmail);
        });

        // Verifica se a mensagem da exceção é a esperada e se o método foi chamado uma vez.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, times(1)).findByEmail(invalidEmail);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void updatePasswordShouldUpdatePassword() {
        // Testa a atualização da senha do usuário.
        // Simula a busca do usuário e a atualização da senha.
        String currentPassword = "senhaSegura123";
        String newPassword = "novaSenha123";
        String confirmationPassword = "novaSenha123";
        when(repository.findById(validUser.getId())).thenReturn(Optional.of(validUser));

        // Chama o método para atualizar a senha.
        service.updatePassword(validId, currentPassword, newPassword, confirmationPassword);

        // Verifica se a senha foi atualizada corretamente.
        assertNotEquals(currentPassword, validUser.getPassword());  // A senha atual não pode ser igual à antiga
        assertEquals(newPassword, validUser.getPassword());  // A nova senha deve ser igual
        verify(repository, times(1)).findById(validId);  // Verifica se o método findById foi chamado uma vez
        verify(repository, times(1)).save(validUser);  // Verifica se o método save foi chamado uma vez
    }

    @Test
    void updatePasswordShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Testa o comportamento quando o usuário não é encontrado ao tentar atualizar a senha.
        String expectedMessage = "Nenhum usuário foi encontrado com o id: " + invalidId;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        // Espera que uma exceção ResourceNotFoundException seja lançada.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.updatePassword(invalidId, "", "", "");
        });

        // Verifica se a mensagem da exceção é a esperada e se o método foi chamado uma vez.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, times(1)).findById(invalidId);  // Verifica se o método foi chamado uma vez
    }

    @Test
    void updatePasswordShouldThrowPasswordInvalidExceptionWhenNewPasswordDoesNotMatchConfirmation() {
        // Testa o comportamento quando a nova senha não confere com a confirmação.
        String currentPassword = "senhaAtual123";
        String newPassword = "novaSenha123";
        String confirmationPassword = "senhaDiferente123";
        String expectedMessage = "Nova senha não confere com confirmação de senha.";

        // Espera que uma exceção PasswordInvalidException seja lançada.
        PasswordInvalidException exception = assertThrows(PasswordInvalidException.class, () -> {
            service.updatePassword(validId, currentPassword, newPassword, confirmationPassword);
        });

        // Verifica se a mensagem da exceção é a esperada.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, never()).findById(anyLong());  // Verifica se o método findById NUNCA foi chamado
    }

    @Test
    void updatePasswordShouldThrowPasswordInvalidExceptionWhenCurrentPasswordIsIncorrect() {
        // Testa o comportamento quando a senha atual fornecida está incorreta.
        String currentPassword = "senhaErrada123";
        String newPassword = "novaSenha123";
        String confirmationPassword = "novaSenha123";
        String expectedMessage = "Sua senha não confere.";

        // Simula a busca do usuário, mas a senha atual não corresponde à fornecida.
        when(repository.findById(validId)).thenReturn(Optional.of(validUser));

        // Espera que uma exceção PasswordInvalidException seja lançada.
        PasswordInvalidException exception = assertThrows(PasswordInvalidException.class, () -> {
            service.updatePassword(validId, currentPassword, newPassword, confirmationPassword);
        });

        // Verifica se a mensagem da exceção é a esperada.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, times(1)).findById(validId);  // Verifica se o método findById foi chamado uma vez
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        // Given / Arrange: Prepara os dados necessários para o teste.
        // Neste caso, configuramos o repositório para retornar Optional.empty() quando um ID inválido é passado.
        String expectedMessage = "Nenhum usuário foi encontrado com o id: " + invalidId;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        // When / Act: Chama o método de serviço para deletar o usuário com ID inválido.
        // Espera-se que uma exceção ResourceNotFoundException seja lançada.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(invalidId);
        });

        // Then / Assert: Verifica se a exceção foi lançada e se a mensagem da exceção é a esperada.
        // Também verifica se o método findById foi chamado uma vez e se o método delete não foi chamado.
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository, times(1)).findById(invalidId);  // Verifica se o método findById foi chamado uma vez.
        verify(repository, times(0)).delete(any(User.class));  // Verifica se o método delete não foi chamado.
    }

    @Test
    void deleteShouldDeleteWhenIdExists(){
        // Given / Arrange: Prepara o teste configurando o repositório para retornar um usuário válido.
        // Também configuramos o repositório para não realizar nenhuma ação quando o método delete for chamado.
        when(repository.findById(validId)).thenReturn(Optional.of(validUser));
        doNothing().when(repository).delete(validUser);

        // When / Act: Chama o método de serviço para deletar o usuário com ID válido.
        service.delete(validId);

        // Then / Assert: Verifica se o método delete foi chamado uma vez para deletar o usuário.
        verify(repository, times(1)).delete(validUser);  // Verifica se o método delete foi chamado uma vez.
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenRepositoryThrowsDataIntegrityViolationException(){
        // Given / Arrange: Prepara o teste simulando que o repositório lança uma DataIntegrityViolationException ao tentar deletar.
        // Também configura o repositório para retornar um usuário válido ao procurar pelo ID.
        String expectedMessage = "Violação de integridade";
        when(repository.findById(validId)).thenReturn(Optional.of(validUser));

        // When / Act: Chama o método de serviço para deletar o usuário e simula uma exceção de violação de integridade de dados.
        doThrow(new DataIntegrityViolationException("Erro de integridade")).when(repository).delete(validUser);

        // Then / Assert: Verifica se uma DatabaseException é lançada quando ocorre uma violação de integridade de dados.
        // Verifica também se a mensagem da exceção é a esperada e se o método delete foi chamado uma vez.
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            service.delete(validId);
        });

        assertEquals(expectedMessage, exception.getMessage());  // Verifica se a mensagem da exceção é a esperada.
        verify(repository, times(1)).delete(validUser);  // Verifica se o método delete foi chamado uma vez.
        verify(repository, times(1)).findById(validId);  // Verifica se o método findById foi chamado uma vez.
    }

}
