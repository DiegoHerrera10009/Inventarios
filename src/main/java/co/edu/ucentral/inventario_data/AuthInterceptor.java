package co.edu.ucentral.inventario_data;

import co.edu.ucentral.inventario_data.Persistencia.Entidades.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

public class AuthInterceptor implements HandlerInterceptor {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/",
            "/login",
            "/registro",
            "/error"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestUri.substring(contextPath.length());

        // Recursos públicos (sin login)
        if (isPublicPath(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        Object usuarioLogueado = (session != null) ? session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(contextPath + "/login");
            return false;
        }

        // Control básico de acceso por rol (ADMIN / COMERCIAL)
        Usuario usuario = (Usuario) usuarioLogueado;
        String rol = usuario.getRol() != null ? usuario.getRol().toUpperCase() : "COMERCIAL";

        // Rutas de gestión de productos solo para ADMIN
        if (path.startsWith("/producto/")) {
            boolean esRutaSoloLectura =
                    path.equals("/producto/lista")
                            || path.startsWith("/producto/ficha");
            if (!esRutaSoloLectura && !"ADMIN".equals(rol)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }

        // Gestión de usuarios solo para ADMIN (por si se agregan más rutas)
        if (path.startsWith("/usuarios") && !"ADMIN".equals(rol)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // Las rutas de salida (movimientos) pueden ser usadas por ADMIN y COMERCIAL

        return true;
    }

    private boolean isPublicPath(String path) {
        if (PUBLIC_PATHS.contains(path)) {
            return true;
        }

        // Permitir login y registro tanto GET como POST
        if (path.startsWith("/login") || path.startsWith("/registro")) {
            return true;
        }

        // Recursos estáticos
        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/uploads/")
                || path.startsWith("/webjars/");
    }
}

