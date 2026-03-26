import { setPageCleanup, runPageCleanup, routes, markup } from "./vars.js";

/**
 *  Match route with path and extract params, iterate through all routes 
 */
function matchRoute(pathname) {
    for (const route in routes) {
        /* get regex pattern from route to match actual URL */
        const regexPath = route.replace(/:([^/]+)/g, "([^/]+)");

        /* Match exactly both beginning and end, return {module,placeholder} */
        const token = pathname.match(new RegExp(`^${regexPath}$`));
        /* slice token from [1] to skip full match and get to the placeholders */
        if (token)
            return { module: routes[route], placeholder: token.slice(1) };
    }
    if (pathname.endsWith("index.html") || pathname.endsWith("index"))
        return { module: routes["/"], placeholder: [] };
    return null;
}

/**
 * route based on current URL
 * 
 * @returns {void}
 */
export async function router(url = null) {
    /* update URL history */
    url && history.pushState({}, "", url);

    // run cleanup of previous page, if provided
    runPageCleanup();

    const match = matchRoute(location.pathname);
    let module;
    if (match) {
        module = await match.module();
        module.default(markup, match.placeholder);
    } else {
        module = await routes["/404"]();
        module.default(markup, []);
    }

    // set page cleanup function, if provided
    setPageCleanup(module.pageCleanup);
}
