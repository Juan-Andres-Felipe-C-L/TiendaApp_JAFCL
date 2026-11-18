package com.JuanAndresFelipe.TiendaApp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AdminsPassword {
    
    public static void main(String[] args) {
        
        /*Instructor Michael, con esta clase fue que obtuve la clave encriptada del administrador del sistema, la cual, debía ser brindada por nosotros en la base de datos y los entregables.*/
        
        BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

        String hash = encoder.encode("Admin123*");

        System.out.println("Esta es la contraseña encriptada: " + hash);
    }
}
