package com.JuanAndresFelipe.TiendaApp.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.JuanAndresFelipe.TiendaApp.dto.ClientRegisterRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.HttpGlobalResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.LoginRequestDTO;
import com.JuanAndresFelipe.TiendaApp.dto.LoginResponseDTO;
import com.JuanAndresFelipe.TiendaApp.dto.UserDTO;
import com.JuanAndresFelipe.TiendaApp.entity.Roles;
import com.JuanAndresFelipe.TiendaApp.entity.Users;
import com.JuanAndresFelipe.TiendaApp.repository.RolesRepository;
import com.JuanAndresFelipe.TiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Método que hace posible que un cliente se registre en la aplicación.
     * 
     * @param request es un DTO con los atributos necesarios para hacer un registro
     *                de un usuario.
     * @return Se devuelve un DTO con cierta información del usuario que se acaba de
     *         registrar y un mensaje que corrobora si el registro fue exitoso.
     */
    public HttpGlobalResponseDTO<UserDTO> register(ClientRegisterRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        Optional<Roles> rolFound = rolesRepository.findById(2L);

        if (usernameFound.isPresent()) {
            response.setMessage("El nombre de usuario ya está en uso.");
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("El correo de usuario ya está en uso.");
            return response;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(rolFound.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(rolFound.get().getName());
        userDTO.setActive(user.isActive());

        response.setMessage("El Usuario ha sido registrado correctamente.");
        response.setData(userDTO);

        return response;
    }

    /**
     * Método que permite que un usuario pueda iniciar sesión con su cuenta de la
     * aplicación.
     * 
     * @param request un DTO con solo los datos del usuario necesarios para poder
     *                hace Login.
     * @return un DTO que arroja, o los datos del usuario y un mensaje de que pudo
     *         iniciar sesión, o mensajes de que las credenciales fueron
     *         incorrectas. Aquí también va incluido el JWT token.
     */
    public HttpGlobalResponseDTO<LoginResponseDTO> login(LoginRequestDTO request) {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> emailFound = usersRepository.findByEmail(request.getUser());
        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUser());

        if (usernameFound.isEmpty() && emailFound.isEmpty()) {
            response.setMessage("Usuario y/o contraseña incorrectos.");
            return response;
        }

        Users userFound;

        if (emailFound.isPresent()) {
            userFound = emailFound.get();
        } else {
            userFound = usernameFound.get();
        }

        if (!passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            response.setMessage("Usuario y/o contraseña incorrectos.");
            return response;
        }

        String jwt = jwtService.generateToken(userFound.getId(), userFound.getUsername(), userFound.getRole().getId());
        LoginResponseDTO token = new LoginResponseDTO();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userFound.getId());
        userDTO.setUsername(userFound.getUsername());
        userDTO.setEmail(userFound.getEmail());
        userDTO.setActive(userFound.isActive());
        userDTO.setRolId(userFound.getRole().getName());

        token.setJwt(jwt);
        token.setUser(userDTO);
        response.setMessage("Inicio de sesión exitoso.");
        response.setData(token);

        return response;
    }

    /**
     * Método para refrescar toke jwt de un usuario que esté autenticado y activo en la aplicación.
     * @param token Jwt en formato de String.
     * @return un DTO con algunos datos del usuario, un mensaje de que el token fue refrescado y token JWT en sí mismo.
     * @throws Exception
     */
    public HttpGlobalResponseDTO<LoginResponseDTO> refreshToken(String token) throws Exception {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        LoginResponseDTO refresh = new LoginResponseDTO();
        
        String jwt = jwtService.refreshToken(token);
        Long userId = jwtService.extractUserId(token);
        
        Optional<Users> userFound = usersRepository.findById(userId);
        Users user = userFound.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(user.getRole().getName());
        userDTO.setActive(user.isActive());
        
        refresh.setJwt(jwt);
        refresh.setUser(userDTO);
        response.setMessage("El Token ha sido refrescado correctamente.");
        response.setData(refresh);
        return response;
    }

}
