import { router } from "./router.js"

document.getElementById("main").innerHTML = "<h3>Loading content...</h3>";

// Navigation handler
document.querySelectorAll("nav button").forEach(btn => {
    btn.addEventListener("click", () => {
        router(btn.dataset.link);  /* render module based on new URL */
    });
});

// Handle back/forward browser buttons, because they will update URL history
window.addEventListener("popstate", () => router(null));

// Initial load for initial URL
router();
