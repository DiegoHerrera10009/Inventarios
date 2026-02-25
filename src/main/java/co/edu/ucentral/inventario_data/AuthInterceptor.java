package co.edu.ucentral.inventario_data;

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

