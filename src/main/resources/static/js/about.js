import { fetch_text, loadCSS } from "./util.js";

export default async function _(markup, placeholder) {
  console.log("Entering About page");
  const main = document.getElementById("main");
  main.innerHTML = "<h3>Loading About page...</h3>";
  await loadCSS("/css/about.css");

  /* load needed resources */
  if (!markup.about)
    markup.about = await fetch_text("/js/html/about.html");

  /* set page UI */
  main.innerHTML = `
      <h1>About Page</h1>
      ${markup.about}
    `;
}

export function pageCleanup() {
  console.log("Leaving About page");
}
