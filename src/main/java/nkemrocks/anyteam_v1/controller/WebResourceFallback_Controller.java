package nkemrocks.anyteam_v1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebResourceFallback_Controller {
    public final ResourceLoader resourceLoader;

    @GetMapping("/**/{path:[^\\.]*}")
    public String redirectIfNotFound(HttpServletRequest request) {
        String requestPath = request.getRequestURI();

        // Check if the requested file exists in /static
        Resource resource = resourceLoader.getResource("classpath:static" + requestPath);
        if (resource.exists() && resource.isReadable()) {
            return null; // Let Spring serve the static file
        }

        // Fallback to index.html
        return "forward:/index.html";
    }
}
