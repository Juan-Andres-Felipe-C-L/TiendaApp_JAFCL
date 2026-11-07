package com.JuanAndresFelipe.TiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.JuanAndresFelipe.TiendaApp.dto.ChangeRoleDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.RegisterRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.UserDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Roles;
import com.JuanAndresFelipe.TiendaApp.entity.Users;
import com.JuanAndresFelipe.TiendaApp.repository.RolesRepository;
import com.JuanAndresFelipe.TiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UsersService {
    
    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;
    
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

    
    /**
     * Método para que los usaurios, tipo "Administrador", puedan crear nuevos usuarios de diferentes tipos de rol. 
     * @param request es un DTO que permite aportar los datos necesarios para crear un usuario en la aplicación.
     * @return un DTO con un mensaje y algunos datos del usuario registrado.
     */
    public HttpGlobalResponseDTO<UserDTO> register(RegisterRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        Optional<Roles> rol = rolesRepository.findById(request.getRoleId());

        if (usernameFound.isPresent()) {
            response.setMessage("El nombre de usuario ya está en uso.");
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("El correo de usuario ya está en uso.");
            return response;
        }

        if (rol.isEmpty()) {
            response.setMessage("El rol no existe.");
            return response;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(rol.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(rol.get().getName());
        userDTO.setActive(user.isActive());

        response.setMessage("Usuario registrado correctamente.");
        response.setData(userDTO);

        return response;
    }

    /**
     * Método para cambiar el rol de un usuario existente en la aplicación.
     * @param id es el I.D. del usuario a quien se le quiere hacer el cambio.
     * @param request es un DTO para ingresar el dato nuevo del I.D. del usuario a quien se quiere actualizar.
     * @return Se retorna un DTO con un mensaje y algunos datos del usuario a quien se le está haciendo la actualización.
     */
    public HttpGlobalResponseDTO<UserDTO> changeId(Long id, ChangeRoleDTO request ) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> userFound = usersRepository.findById(id);
        Optional<Roles> roleFound = rolesRepository.findById(request.getNewRoleId());

        if (userFound.isEmpty()) {
            response.setMessage("No existe un usuario registrado en esta aplicación con ese I.D. escrito.");
            return response;
        }

        if (roleFound.isEmpty()) {
            response.setMessage("El tipo de rol que ha escrito para el usuario no existe.");
            return response;
        }

        Users user = userFound.get();
        user.setRole(roleFound.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(user.getRole().getName());
        userDTO.setActive(user.isActive());

        response.setMessage("I.D. de usuario actualizado correctamente.");
        response.setData(userDTO);

        return response;    
    }
}
