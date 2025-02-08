package br.edu.ifba.xpnewsbackend.user.mapper;

import br.edu.ifba.xpnewsbackend.user.dto.UserCreateDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserResponseDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserUpdateDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    /**
     * Converte um DTO de criação de usuário para uma entidade User.
     * @param createDto Objeto DTO contendo os dados de criação do usuário.
     * @return Entidade User correspondente.
     */
    public static User toUser(UserCreateDto createDto) {
        return new ModelMapper().map(createDto, User.class);
    }

    /**
     * Converte um DTO de atualização de usuário para uma entidade User.
     * @param updateDto Objeto DTO contendo os dados de atualização do usuário.
     * @return Entidade User atualizada com os dados do DTO.
     */
    public static User updateToUser(UserUpdateDto updateDto) {
        return new ModelMapper().map(updateDto, User.class);
    }

    /**
     * Converte uma entidade User para um DTO de resposta, formatando o papel do usuário.
     * @param user Entidade User a ser convertida.
     * @return DTO de resposta contendo as informações do usuário formatadas.
     */
    public static UserResponseDto toDto(User user) {
        // Remove o prefixo "ROLE_" do nome do papel do usuário
        String role = user.getRole().name().substring("ROLE_".length());

        // Mapeamento personalizado para garantir a conversão correta do papel do usuário
        PropertyMap<User, UserResponseDto> props = new PropertyMap<User, UserResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(user, UserResponseDto.class);
    }

    /**
     * Converte uma lista de entidades User para uma lista de DTOs de resposta.
     * @param users Lista de entidades User a serem convertidas.
     * @return Lista de DTOs de resposta.
     */
    public static List<UserResponseDto> toListDto(List<User> users) {
        return users.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
