package co.edu.ucentral.inventario_data.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioControlador {

    @GetMapping("/")
    public String mostrarInicio() {
        return "inicio"; // apunta al archivo inicio.html
    }
}
