/* define global vars/setters/runners all modules may need */

/** cleanup function */
let pageCleanup = null;

/**  set cleanup function
 * 
 * @param {function(): void} currentPageCleanupFunc
 */
export function setPageCleanup(currentPageCleanupFunc) {
    if (typeof currentPageCleanupFunc === "function")
        pageCleanup = currentPageCleanupFunc;
}

/**  run cleanup function */
export function runPageCleanup() {
    if (typeof pageCleanup === "function") {
        pageCleanup();
        pageCleanup = null;
    }
}

/** All available routes */
export const routes = {
    "/": () => import("./home.js"),
    "/about": () => import("./about.js"),
    "/404": () => import("./404.js"),
};

/** Markup object, passed to every route page to be potentially updated */
export const markup = {};
