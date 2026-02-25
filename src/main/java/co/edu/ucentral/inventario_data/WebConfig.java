package co.edu.ucentral.inventario_data;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve los archivos de imagen subidos desde la carpeta "uploads" en el sistema de archivos
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor simple basado en sesión para exigir login en las rutas protegidas
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**");
    }
}

