export default async function _(markup, placeholder) {
    console.log("Entering 404 page");
    const main = document.getElementById("main");
    /* set page UI */
    main.innerHTML = `<h1>404, Not Found</h1>`;
}

export function pageCleanup() {
    console.log("Leaving 404 page");
  }
  