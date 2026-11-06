package com.JuanAndresFelipe.TiendaApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.RegisterRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.UserDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Roles;
import com.JuanAndresFelipe.TiendaApp.entity.Users;
import com.JuanAndresFelipe.TiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UsersService {
    
    private final UsersRepository usersRepository;
    
    /**
     * Método para obtener todos los usuarios registrados en la aplicación.
     * @return un DTO con un mensaje y una lista de usuarios con algunos de sus datos. 
     */
    public HttpGlobalResponseDTO<List<UserDTO>> getUsers() {
        HttpGlobalResponseDTO<List<UserDTO>> response = new HttpGlobalResponseDTO<>();
        List<UserDTO> listUsers = new ArrayList<>();

        List<Users> usersFound = usersRepository.findAll();

        for (Users u : usersFound) {
            UserDTO user = new UserDTO();
            user.setId(u.getId());
            user.setUsername(u.getUsername());
            user.setEmail(u.getEmail());
            user.setRolId(u.getRole().getName());
            user.setActive(u.isActive());

            listUsers.add(user);
        }

        response.setMessage("Estos son los usuarios encontrados.");
        response.setData(listUsers);

        return response;
    }

    

    public HttpGlobalResponseDTO<UserDTO> register(RegisterRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        Optional<Roles> rol = rolesRepository.findById(request.getRolId());

        if (usernameFound.isPresent()) {
            response.setMessage("El nombre de usuario ya está en uso");
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("El correo de usuario ya está en uso");
            return response;
        }

        if (rol.isEmpty()) {
            response.setMessage("El rol no existe");
            return response;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(rol.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(rol.get().getName());

        response.setMessage("Usuario registrado correctamente");
        response.setData(userDTO);

        return response;
    }
}
