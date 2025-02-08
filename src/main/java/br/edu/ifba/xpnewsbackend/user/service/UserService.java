package br.edu.ifba.xpnewsbackend.user.service;

import br.edu.ifba.xpnewsbackend.infrastructure.exception.DatabaseException;
import br.edu.ifba.xpnewsbackend.infrastructure.exception.PasswordInvalidException;
import br.edu.ifba.xpnewsbackend.infrastructure.exception.ResourceNotFoundException;
import br.edu.ifba.xpnewsbackend.infrastructure.exception.UniqueViolationException;
import br.edu.ifba.xpnewsbackend.infrastructure.clients.UserClient;
import br.edu.ifba.xpnewsbackend.infrastructure.dto.PageableDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import br.edu.ifba.xpnewsbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserIService{

    private final UserRepository repository;
    private final UserClient userClient;

    /**
     * Cria um novo usuário no banco de dados.
     * Caso o username ou email já estejam cadastrados, lança uma exceção de violação de unicidade.
     * @param user Objeto do usuário a ser salvo.
     * @return Usuário salvo no banco de dados.
     */
    @Override
    @Transactional
    public User create(User user) {
        log.info("Criando um novo usuário com username: {}", user.getUsername());
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Erro ao criar usuário: já existe um usuário com este username ou email");
            throw new UniqueViolationException("Já tem um usuário registrado com esse username ou email");
        }
    }

    /**
     * Retorna todos os usuários paginados.
     * @param pageable Objeto de paginação para organizar os resultados.
     * @return Página contendo os usuários encontrados.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserProjection> findAll(Pageable pageable) {
        log.info("Buscando todos os usuários paginados");
        return repository.findAllPageable(pageable);
    }

    /**
     * Busca um usuário pelo ID fornecido.
     * Caso o usuário não seja encontrado, lança uma exceção de recurso não encontrado.
     * @param id ID do usuário a ser buscado.
     * @return Usuário encontrado no banco de dados.
     */
    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        log.info("Buscando usuário por ID: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("Usuário não encontrado com o ID: {}", id);
            return new ResourceNotFoundException("Nenhum usuário foi encontrado com o id: " + id);
        });
    }

    /**
     * Busca um usuário pelo username fornecido.
     * Caso o usuário não seja encontrado, lança uma exceção de recurso não encontrado.
     * @param username Username do usuário a ser buscado.
     * @return Usuário encontrado no banco de dados.
     */
    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        log.info("Buscando usuário por username: {}", username);
        return repository.findByUsername(username).orElseThrow(() -> {
            log.warn("Usuário não encontrado com o username: {}", username);
            return new ResourceNotFoundException("Nenhum usuário foi encontrado com o username: " + username);
        });
    }

    /**
     * Busca um usuário pelo email fornecido.
     * Caso o usuário não seja encontrado, lança uma exceção de recurso não encontrado.
     * @param email Email do usuário a ser buscado.
     * @return Usuário encontrado no banco de dados.
     */
    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        log.info("Buscando usuário por email: {}", email);
        return repository.findByEmail(email).orElseThrow(() -> {
            log.warn("Usuário não encontrado com o email: {}", email);
            return new ResourceNotFoundException("Nenhum usuário foi encontrado com o email: " + email);
        });
    }

    /**
     * Atualiza a senha do usuário após validar a senha atual e a confirmação da nova senha.
     * Caso a senha atual seja inválida ou a confirmação não coincida, lança exceções apropriadas.
     * @param id ID do usuário que terá a senha alterada.
     * @param currentPassword Senha atual do usuário.
     * @param newPassword Nova senha a ser cadastrada.
     * @param confirmationPassword Confirmação da nova senha.
     */
    @Override
    @Transactional
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmationPassword) {
        log.info("Atualizando senha do usuário com ID: {}", id);
        if (!newPassword.equals(confirmationPassword)) {
            log.warn("Nova senha e confirmação de senha não conferem para o usuário com ID: {}", id);
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
        }

        User user = findById(id);
        if (!user.getPassword().equals(currentPassword)) {
            log.warn("Senha atual incorreta para o usuário com ID: {}", id);
            throw new PasswordInvalidException("Sua senha não confere.");
        }

        user.setPassword(newPassword);
        repository.save(user);
        log.info("Senha atualizada com sucesso para o usuário com ID: {}", id);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User newData) {
        log.info("Iniciando atualização do usuário com ID: {}", id);
        User user = findById(id);

        user.setUsername(newData.getUsername());
        user.setEmail(newData.getEmail());
        user.setRole(newData.getRole());
        user.setFullName(newData.getFullName());
        log.info("Usuário com id {} encontrado.", id);

        repository.save(user);
        log.info("Usuário com ID {} atualizado com sucesso", id);
    }


    /**
     * Exclui um usuário do banco de dados após encontrá-lo pelo ID.
     * Caso haja uma violação de integridade, lança uma exceção apropriada.
     * @param id ID do usuário a ser excluído.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Excluindo usuário com ID: {}", id);
        User user = findById(id);

        try{
            repository.delete(user);
            log.info("Usuário com ID: {} deletado com sucesso", id);
        }catch (DataIntegrityViolationException e){
            log.error("Erro ao excluir usuário com ID: {} - Violação de integridade", id);
            throw new DatabaseException("Violação de integridade");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageableDto findAllWithClient(){
        return userClient.findAll();
    }
}
